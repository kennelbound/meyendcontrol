import static groovyx.javafx.GroovyFX.start
import javafx.animation.AnimationTimer
import javafx.scene.layout.HBox
import javafx.scene.paint.Color
import javafx.scene.canvas.GraphicsContext
import javafx.scene.shape.ArcType
import javafx.scene.text.Font
import javafx.scene.text.TextAlignment
import javafx.scene.canvas.Canvas

start {
    // Register our clock component
    registerBeanFactory "ahbox", AnimationHBox
    registerBeanFactory "clock", ArcClock

    // And add 3 of them in a row
    stage( title: 'Clock', visible: true ) {
        def primaryStage = scene( fill:BLACK ) {
            ahbox {
                clock numArcs:20, longPieceColor:rgba(126, 166, 212, 0.6), manyPieceColor:rgba(126, 166, 222, 0.5), maxDiameter: 200
                clock numArcs:20, longPieceColor:rgba(126, 166, 212, 0.6), manyPieceColor:rgba(130, 230, 166, 0.5), maxDiameter: 200
                clock numArcs:20, longPieceColor:rgba(126, 166, 212, 0.6), manyPieceColor:rgba(230, 130, 166, 0.5), maxDiameter: 200
            }
        }
    }
}

class AnimationHBox extends HBox {
    AnimationHBox() {
        ( [ handle:{ long now ->
            children*.update( now )
            children*.draw()
        } ] as AnimationTimer ).start()
    }
}

// The clock component
class ArcClock extends Canvas {
    static Random rnd = new Random()
    List arcPieces
    int maxDiameter, numArcs
    double radius
    Color longPieceColor, manyPieceColor

    boolean initialised = false

    Font large = Font.font( "Calibri", 40 )
    Font smaller = Font.font( "Calibri", 20 )

    ArcClock() {
    }

    private void initialise() {
        arcPieces = [ [ x:0, y:0, w:maxDiameter, h:maxDiameter, start:45, extent:240, width:5, pixels:1, dir:1, color:longPieceColor, startTime:0 ] ]
        (1..numArcs).each {
            arcPieces << randomArcPiece( manyPieceColor, maxDiameter / 2 )
        }
        initialised = true
    }

    void setMaxDiameter( int maxDiameter ) {
        this.width = maxDiameter
        this.height = maxDiameter
        this.maxDiameter = maxDiameter
        this.radius = maxDiameter / 2
    }

    void update( long now ){
        if( !initialised ) {
            initialise()
        }

        arcPieces.each { arc ->
            if( !arc.startTime ) {
                arc.startTime = now
            }

            long elapsed = now - arc.startTime
            if( elapsed > 60000000 ) {
                arc.start += arc.pixels
                if( arc.start > 360 ){
                    arc.start = 0
                }
                arc.startTime = 0;
            }
        }
    }

    void draw() {
        this.graphicsContext2D.with { gc ->
            gc.clearRect( 0, 0, maxDiameter, maxDiameter )
            arcPieces.each { arc ->
                gc.stroke = arc.color
                gc.lineWidth = arc.width
                gc.strokeArc( arc.x, arc.y, arc.w, arc.h, arc.dir * arc.start, arc.extent, ArcType.OPEN )
            }
            gc.font = large
            gc.fill = Color.WHITE
            gc.textAlign = TextAlignment.CENTER
            Calendar.instance.with { Calendar cal ->
                String hour   = "${cal[ HOUR ] ?: 12}".padLeft( 2, '0' )
                String minute = "${cal[ MINUTE ]}".padLeft( 2, '0' )
                String second = "${cal[ SECOND ]}".padLeft( 2, '0' )
                gc.fillText( hour, radius, radius + 18 )
                gc.font = smaller
                gc.fillText( "$minute ${cal[ AM_PM ]==PM?'PM':'AM'}", maxDiameter - 40, radius - 40 )
                gc.fillText( second, maxDiameter - 40, maxDiameter - 40 )
            }
        }
    }

    static int randomIntRange(int min, int max) {
        rnd.nextInt( max - min + 1 ) + min
    }

    static randomArcPiece( Color color, double radius ) {
        int width =  randomIntRange( 60, (int)radius * 2 )
        int randomStartAngle = randomIntRange( 1, 270 )
        [ x:radius - ( width / 2 ),
          y:radius - ( width / 2 ),
          w:width,
          h:width,
          start:randomStartAngle,
          extent:randomIntRange( 10, 360-randomStartAngle ),
          width:randomIntRange( 1,10 ),
          pixels:randomIntRange( 1, 8 ),
          dir:rnd.nextBoolean() ? -1 : 1,
          color:color,
          startTime:0 ]
    }
}