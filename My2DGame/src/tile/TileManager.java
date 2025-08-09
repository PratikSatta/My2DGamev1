package tile;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.IconifyAction;

import main.GamePanel;
import main.UtilityTool;

public class TileManager {
	
	GamePanel gp;
	public Tile[] tile;
	public int mapTileNum[][][];
	boolean drawPath = true;
	
	public TileManager(GamePanel gp) {
		this.gp = gp;
		
		tile = new Tile[50];
		mapTileNum = new int[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow];
		
		
		getTileImage();
		
		loadMap("/maps/worldV3.txt", 0);
		loadMap("/maps/interior01.txt", 1);
	}
	
	public void getTileImage() {
		
		// PLACEHOLDER
        int indexes[] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        for(int i = 0; i < indexes.length; i++){
            setup(indexes[i], "grass00", false);
        }
        
        // TILES
        setup(10, "grass00", false);
        setup(11, "grass01", false);

        // WATER loop
        indexes = new int[]{12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25};
        for(int i = 0; i < indexes.length; i++){
            String waterIndex = "water" + String.format("%02d", i);
            setup(indexes[i], waterIndex, true);
        }

        // ROAD loop
        indexes = new int[]{26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38};
        for(int i = 0; i < indexes.length; i++){
            String roadIndex = "road" + String.format("%02d", i);
            setup(indexes[i], roadIndex, false);
        }

        setup(39, "earth", false);
        setup(40, "wall", true);
        setup(41,"tree", true);
        setup(42,"hut", false);
        setup(43,"floor01", false);
        setup(44,"table01", true);
		
	}
	
	public void setup(int index, String imageName, boolean collision) {
		
		UtilityTool uTool = new UtilityTool();
		
		try {
			tile[index] = new Tile();
			tile[index].image = ImageIO.read(getClass().getResourceAsStream("/tiles/" + imageName + ".png"));
			tile[index].image = uTool.scaleImage(tile[index].image, gp.tileSize, gp.tileSize);
			tile[index].collision = collision;

		}
		catch (IOException e) {
			e.printStackTrace();

		}
	}
	
	public void loadMap(String filePath, int map) {
		
		try {
			
			InputStream is = getClass().getResourceAsStream(filePath);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			
			int col = 0;
			int row = 0;
			
			 while(col < gp.maxWorldCol && row < gp.maxWorldRow) {
				 
				 String lineString = br.readLine();
				 
				 while(col < gp.maxWorldCol) {
					 
					 String numbers[] =lineString.split(" ");
					 
					 int num = Integer.parseInt(numbers[col]);
					 
					 mapTileNum[map][col][row] = num;
					 col++;
				 }
				 if (col == gp.maxWorldCol) {
					col = 0;
					row++;
				}
				 
			 }
			 br.close();
					
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
	}
	
	public void draw(Graphics2D g2) {
		
		int worldCol = 0;
		int worldRow = 0;
		
		
		while(worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {
			
			int tileNum = mapTileNum[gp.currentMap][worldCol][worldRow]; 
			
			int worldX = worldCol * gp.tileSize;
			int worldY = worldRow * gp.tileSize;
			int screenX = worldX - gp.player.worldX + gp.player.screenX;
			int screenY = worldY - gp.player.worldY + gp.player.screenY;
			
			if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
					worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
					worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
					worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
				
				g2.drawImage(tile[tileNum].image, screenX, screenY, null);

			}
			worldCol++;
			
			
			if (worldCol == gp.maxWorldCol) {
				worldCol=0;
				worldRow ++;
				
				
			}
		}
		if (drawPath == true) {
			g2.setColor(new Color(225,0,0,70));
			
			for(int i =0; i < gp.pFinder.pathList.size(); i++) {
				
				int worldX = gp.pFinder.pathList.get(i).col * gp.tileSize;
				int worldY = gp.pFinder.pathList.get(i).row * gp.tileSize;
				int screenX = worldX - gp.player.worldX + gp.player.screenX;
				int screenY = worldY - gp.player.worldY + gp.player.screenY;
				
				g2.fillRect(screenX, screenY, gp.tileSize, gp.tileSize);
			}
		}
		
	}
	
	
}
