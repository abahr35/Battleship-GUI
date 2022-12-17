import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
//Austin Bahr
public class Ship {

    //Ship state for coloring
    public enum ShipPlacementState {Valid, Invalid, Placed}

    //ship position
    private Point gridPosition;

    //pixel position for drawing
    private Point drawPosition;

    //shows how long the ship is
    private final int segments;

    //rotation, true is Horizontal
    private boolean isSideways;

    //record of the destroyed sections to be compared
    private int destroyedSections;

    //for drawing
    private ShipPlacementState shipPlacementState;


    //creates ship based on size, rotation, amd location
    public Ship(Point gridPosition, Point drawPosition, int segments, boolean isSideways) {

        this.gridPosition = gridPosition;
        this.drawPosition = drawPosition;
        this.segments = segments;
        this.isSideways = isSideways;
        destroyedSections = 0;
        shipPlacementState = ShipPlacementState.Placed;
    }


    //draws based on rotation and shipState
    public void Draw(Graphics g) {
        if(shipPlacementState == ShipPlacementState.Placed) {
            g.setColor(destroyedSections >= segments ? Color.RED : Color.DARK_GRAY);
        } else {
            g.setColor(shipPlacementState == ShipPlacementState.Valid ? Color.GREEN : Color.RED);
        }
        if(isSideways) paintHorizontal(g);
        else paintVertical(g);
    }

    //set color bases on state
    public void setShipPlacementColor(ShipPlacementState shipPlacementState) {
        this.shipPlacementState = shipPlacementState;
    }

    //switch sideways
    public void toggleSideways() {
        isSideways = !isSideways;
    }

    //adds to destroyed section
    public void destroySection() {
        destroyedSections++;
    }

    //checks if entire ship is destroyed
    public boolean isDestroyed() { return destroyedSections >= segments; }

    //changes position to draw
    public void setDrawPosition(Point gridPosition, Point drawPosition) {
        this.drawPosition = drawPosition;
        this.gridPosition = gridPosition;
    }

    //get sideways
    public boolean isSideways() {
        return isSideways;
    }

    //get sips size
    public int getSegments() {
        return segments;
    }

    //draws ship and points rectangle towards face
    //todo maybe add pictures and tint colors for incorrect placement
    public void paintVertical(Graphics g) {
        int shipWidth = (int)(TileSet.Tile_Size * 0.8);
        int shipLeftX = drawPosition.x + TileSet.Tile_Size / 2 - shipWidth / 2;

        try {
            File path = new File("images");
            BufferedImage imageFace = ImageIO.read(new File(path, "ShipFaceV.png"));
            BufferedImage imageEnd = ImageIO.read(new File(path, "ShipEndV.png"));
            g.drawImage(imageFace, shipLeftX, drawPosition.y, shipWidth, TileSet.Tile_Size, null);
            for (int i = 0; i < segments; i++) {
                if(i < segments - 1) {
                    g.drawImage(imageEnd, shipLeftX, drawPosition.y + TileSet.Tile_Size, shipWidth, TileSet.Tile_Size, null);
                }else {
                    g.drawImage(imageEnd, shipLeftX, drawPosition.y + TileSet.Tile_Size, shipWidth, (int) (TileSet.Tile_Size * (segments - 1.2)), null);
                }
            }
        }catch (IOException e){
            g.fillPolygon(new int[]{drawPosition.x + TileSet.Tile_Size /2,shipLeftX,shipLeftX+shipWidth}, new int[]{drawPosition.y+ TileSet.Tile_Size /4,drawPosition.y+ TileSet.Tile_Size,drawPosition.y+ TileSet.Tile_Size},3);
            g.fillRect(shipLeftX,drawPosition.y + TileSet.Tile_Size, shipWidth, (int)(TileSet.Tile_Size * (segments-1.2)));
        }
    }

    //same as above
    public void paintHorizontal(Graphics g) {
        int shipWidth = (int) (TileSet.Tile_Size * 0.8);
        int shipTopY = drawPosition.y + TileSet.Tile_Size / 2 - shipWidth / 2;

        try {
            File path = new File("images");
            BufferedImage imageFace = ImageIO.read(new File(path, "ShipFaceH.png"));
            BufferedImage imageEnd = ImageIO.read(new File(path, "ShipEndH.png"));
            g.drawImage(imageFace, drawPosition.x, shipTopY, TileSet.Tile_Size, shipWidth, null);

            for (int i = 0; i < segments; i++) {
                if (i < segments - 1) {
                    g.drawImage(imageEnd, drawPosition.x + TileSet.Tile_Size, shipTopY, TileSet.Tile_Size, shipWidth, null);
                } else {
                    g.drawImage(imageEnd, drawPosition.x + TileSet.Tile_Size, shipTopY, (int) (TileSet.Tile_Size * (segments - 1.2)), shipWidth, null);
                }
            }

        } catch (IOException e) {

            g.fillPolygon(new int[]{drawPosition.x + TileSet.Tile_Size / 4, drawPosition.x + TileSet.Tile_Size, drawPosition.x + TileSet.Tile_Size}, new int[]{drawPosition.y + TileSet.Tile_Size / 2, shipTopY, shipTopY + shipWidth}, 3);
            g.fillRect(drawPosition.x + TileSet.Tile_Size, shipTopY, (int) (TileSet.Tile_Size * (segments - 1.2)), shipWidth);

        }
    }
}
