package com.kennelbound

import com.kennelbound.util.GroovyTimerTask
import com.theeyetribe.client.GazeManager
import com.theeyetribe.client.ICalibrationProcessHandler
import com.theeyetribe.client.ICalibrationResultListener
import com.theeyetribe.client.ITrackerStateListener
import com.theeyetribe.client.data.CalibrationResult
import com.theeyetribe.client.data.Point2D
import groovy.util.logging.Log4j

import java.awt.Dimension
import java.awt.Toolkit

/**
 * Created by samalsto on 2/27/15.
 */
@Log4j
class CalibrationManager implements ICalibrationResultListener, ICalibrationProcessHandler, ITrackerStateListener {
    private static double TARGET_PADDING = 0.1;
    private static int NUM_MAX_CALIBRATION_ATTEMPTS = 3;
    private static int NUM_MAX_RESAMPLE_POINTS = 3;
    private static int sampleTimeMs = 750;

    private boolean trackerStateOK = false;
    private boolean isAborting = false;

    private int count = 9;
    private int latencyMs = 500;
    private int transitionTimeMs = 750;
    private int reSamplingCount;
    private List<Point2D> points = new ArrayList<Point2D>();
    private Point2D currentPoint;
    private Timer timer = new Timer();

    /*
        Public Methods
     */

    void init() {
        GazeManager.instance.addCalibrationResultListener(this);
    }

    void calibrate() {
        reSamplingCount = 0;
        points = createPointList();

        log.debug "Found points $points"
        GazeManager.instance.calibrationStart(points.size(), this);
    }

    Collection<Point2D> createPointList() {
        log.info "Creating the points list."
        Dimension size = Toolkit.defaultToolkit.screenSize;

        double screenWidth = size.width;
        double screenHeight = size.height;

        double scaleW = 1.0;
        double scaleH = 1.0;
        double offsetX = 0.0;
        double offsetY = 0.0;

        // add some padding
        double paddingHeight = TARGET_PADDING;
        double paddingWidth = (screenHeight * TARGET_PADDING) / screenWidth;
        // use the same distance for the width padding

        int rows, columns;
        rows = columns = Math.sqrt(count);

        if (count == 12) {
            columns = Math.round(columns + 1);
            rows = Math.round(rows);
        }

        log.debug "Columns $columns and rows $rows"

        (0..columns - 1).each { dirX ->
            (0..rows - 1).each { dirY ->
                double x = lerp(paddingWidth, 1 - paddingWidth, dirX / (columns - 1));
                double y = lerp(paddingHeight, 1 - paddingHeight, dirY / (rows - 1));
                Point2D point = new Point2D(offsetX + x * scaleW, offsetY + y * scaleH);

                log.debug "Adding point $point";

                points.add(point);
            }
        }

        // Shuffle point order
        Collection<Point2D> calibrationPoints = new ArrayList<Point2D>();
        def order = [];

        (0..count - 1).each { int i ->
            order[i] = i;
        }

        Collections.shuffle(order);

        log.debug "Using order $order";

        order.each { int number ->
            log.debug "Trying number $number and point ${points[number]}"
            Point2D point = points[number];
            point.x *= size.width;
            point.y *= size.height;
            calibrationPoints.add(point);
        }

        return calibrationPoints;
    }

    Point2D pickNextPoint() {
        return points.empty ? null : points.remove(0);
    }

    void calibratePoint() {
        currentPoint = pickNextPoint();

        if (currentPoint) {
            log.info("Starting calibration for $currentPoint");
            GazeManager.instance.calibrationPointStart((int) currentPoint.x, (int) currentPoint.y);
        }
    }

    private static double lerp(double value1, double value2, double amount) {
        return value1 + (value2 - value1) * amount;
    }

    /*
        Implementation: ICalibrationResultListener
     */

    @Override
    void onCalibrationChanged(boolean b, CalibrationResult calibrationResult) {
        log.info "Calibration changed to $b, with result $calibrationResult";
    }

    /*
        Implementation: ICalibrationProcessListener
     */

    @Override
    void onCalibrationStarted() {
        log.info("Calibration Starting...");

        // Setup the current point
        pickNextPoint();

        log.info("Drawing initial calibration point at $currentPoint")

        def self = this;
        def task = new GroovyTimerTask(closure: {
            log.info("Calibrating a point.");
            self.calibratePoint();
        });
        timer.scheduleAtFixedRate(task, 1000, sampleTimeMs);
    }

    @Override
    void onCalibrationProgress(double progress) {
        log.info("Calibration at ${progress}% ...");
        if (progress == 1.0) // done
            return;

        // transition to next point
        Point2D nextPos = pickNextPoint();
        GazeManager.instance.calibrationPointEnd();

        if (nextPos == null) { // no more points?
            timer.cancel();
            return;
        }

        // Store next point as current (global)
        currentPoint = nextPos;

        log.info("Calibrating $currentPoint.x , $currentPoint.y");
    }

    @Override
    void onCalibrationProcessing() {
        log.info("Calibration data processing ...");
    }

    @Override
    void onCalibrationResult(CalibrationResult res) {
        log.info("Calibration complete with ${calibResult.result ? 'success' : 'failure'}...");

        // No result?
        if (res == null || res.calibpoints == null) {
            throw Exception("Calibration result is empty.");
            return;
        }

        log.info("CalibrationResult, avg: " + res.averageErrorDegree + " left: " + res.averageErrorDegreeLeft + " right: " + res.averageErrorDegreeRight);

        // Success, check results for bad points
        res.calibpoints.each { CalibrationResult.CalibrationPoint cp ->
            if (cp != null && cp.coordinates != null) {
                // Tracker tells us to resample this point, enque it
                if (cp.state == CalibrationResult.CalibrationPoint.STATE_RESAMPLE || cp.state == CalibrationResult.CalibrationPoint.STATE_NO_DATA) {
                    points.push(new Point2D(cp.coordinates.x, cp.coordinates.y));
                }
            }
        }

        // Time to stop?
        if (reSamplingCount++ > NUM_MAX_CALIBRATION_ATTEMPTS || points.size() > NUM_MAX_RESAMPLE_POINTS) {
            GazeManager.instance.calibrationAbort();
            throw new Exception("Unable to calibrate");
            return;
        }

        // Resample?
        if (points != null && points.size() > 0) {
            // TODO: Resample logic
        }
    }

    /*
        Implementation: ITrackerStateChangedListener
     */

    @Override
    void onTrackerStateChanged(int trackerState) {
        trackerStateOK = false;
        String errorMessage = "";

        switch (trackerState) {
            case GazeManager.TrackerState.TRACKER_CONNECTED:
                trackerStateOK = true;
                break;
            case GazeManager.TrackerState.TRACKER_CONNECTED_NOUSB3:
                errorMessage = "Device connected to a USB2.0 port";
                break;
            case GazeManager.TrackerState.TRACKER_CONNECTED_BADFW:
                errorMessage = "A firmware updated is required.";
                break;
            case GazeManager.TrackerState.TRACKER_NOT_CONNECTED:
                errorMessage = "Device not connected.";
                break;
            case GazeManager.TrackerState.TRACKER_CONNECTED_NOSTREAM:
                errorMessage = "No data coming out of the sensor.";
                break;
        }

        if (trackerStateOK || isAborting)
            return;

        if (!trackerStateOK) {
            // Lost device, abort calib now (raise event)
            throw new Exception("Aborting Calibration as the device is gone:  $errorMessage");
        }
    }

    @Override
    void onScreenStatesChanged(int screenIndex, int screenResolutionWidth, int screenResolutionHeight, float screenPhysicalWidth, float screenPhysicalHeight) {
        log.info("Screen states have changed to index: $screenIndex, width: $screenResolutionWidth, height: $screenResolutionHeight, physical width: $screenPhysicalWidth, physical height: $screenPhysicalHeight");
    }
}
