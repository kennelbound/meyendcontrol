package com.kennelbound.components

import com.theeyetribe.client.GazeManager
import com.theeyetribe.client.IGazeListener
import com.theeyetribe.client.data.GazeData
import com.theeyetribe.client.data.Point2D
import groovy.util.logging.Log4j
import javafx.animation.AnimationTimer
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.Line

/**
 * Created by samalsto on 3/2/15.
 */
@Log4j
class TrackBox extends Pane implements IGazeListener {
    double padding = 25;

    ImageView leftEye = null;
    ImageView rightEye = null;

    Line viewLine = null;

    ImageView trackingLost = null;
    ImageView successOverlay = null;
    ImageView failureOverlay = null;

    TrackBox() {
        super();

        viewLine = new Line();
        viewLine.fill = Color.RED;
        viewLine.visible = false;

        leftEye = createEye();
        leftEye.x = 0;
        leftEye.y = 0;
        leftEye.visible = false;

        rightEye = createEye();
        rightEye.x = leftEye.x + 1;
        rightEye.y = leftEye.y;
        rightEye.visible = false;

        trackingLost = createImageView("/com.kennelbound.mc.mouse.images/trackinglost.png");
        trackingLost.visible = true;

        successOverlay = createImageView("/com.kennelbound.mc.mouse.images/gradient_green_128.png");
        successOverlay.visible = false;
        successOverlay.x = 0;
        successOverlay.y = 0;

        failureOverlay = createImageView("/com.kennelbound.mc.mouse.images/gradient_red_128.png");
        failureOverlay.x = 0;
        failureOverlay.y = 0;
        failureOverlay.visible = true;

        this.children.addAll(leftEye, rightEye, trackingLost, successOverlay, failureOverlay);
    }

    ImageView createEye() {
        ImageView imageView = createImageView("/com.kennelbound.mc.mouse.images/tb_eye_128.png")
        imageView.fitWidth = 25;
        return imageView;
    }

    ImageView createImageView(String imageLocation) {
        Image image = new Image(imageLocation);
        ImageView imageView = new ImageView(image)
        imageView.preserveRatio = true
        imageView.smooth = true;
        imageView.cache = true;
        return imageView;
    }

    void determineState() {
        boolean lesafe = leftEye.x >= padding || leftEye.y >= padding;
        boolean resafe = rightEye.x >= padding || rightEye.y >= padding;

        if (lesafe || resafe) {
            successOverlay.visible = true;
            failureOverlay.visible = false;
            trackingLost.visible = false;
        } else if (lesafe && !resafe || !lesafe && resafe) {
            successOverlay.visible = false;
            failureOverlay.visible = true;
            trackingLost.visible = false;
        } else {
            failureOverlay.visible = true;
            trackingLost.visible = true;
            successOverlay.visible = false;
        }
    }

    @Override
    void onGazeUpdate(GazeData gazeData) {
        if (!this.width) {
            return;
        }

        // size the overlays (TODO: Figure out a way to do this only once)
        successOverlay.fitWidth = this.width
        successOverlay.fitHeight = this.height
        failureOverlay.fitWidth = this.width
        failureOverlay.fitHeight = this.height
        trackingLost.fitWidth = 50

        def maxWidth = GazeManager.instance.screenResolutionWidth
        def maxHeight = GazeManager.instance.screenResolutionHeight

        def multiplierW = this.width / maxWidth;
        def multiplierH = this.height / maxHeight;

        leftEye.x = gazeData.leftEye.pupilCenterCoordinates.x * maxWidth * multiplierW;
        leftEye.y = gazeData.leftEye.pupilCenterCoordinates.y * maxHeight * multiplierH;
        leftEye.visible = leftEye.x > 0 && leftEye.y > 0;

        rightEye.x = gazeData.rightEye.pupilCenterCoordinates.x * maxWidth * multiplierW;
        rightEye.y = gazeData.rightEye.pupilCenterCoordinates.y * maxHeight * multiplierH;
        rightEye.visible = rightEye.x > 0 && rightEye.y > 0;

        def gazeX = gazeData.smoothedCoordinates.x * multiplierW
        def gazeY = gazeData.smoothedCoordinates.y * multiplierH

        viewLine.startX = rightEye.x;
        viewLine.startY = rightEye.y;

        viewLine.endX = gazeX;
        viewLine.endY = gazeY;

        viewLine.visible = viewLine.startX && viewLine.startY && viewLine.endX && viewLine.endY;

        determineState();
    }
}

