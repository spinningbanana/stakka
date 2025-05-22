package ui.application;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import java.io.File;
import java.io.IOException;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.imageio.ImageIO;

import model.Game;
import model.Seg;
import model.pieces.Piece;

/*
 * The UI element where the board is rendered
 */
public class Board extends JPanel {
    private static final String DEFAULT_CELL_TEXTURE = "cells_default.png";

    public static final String FONT = Font.MONOSPACED;

    public static final int MARGIN = 20;
    public static final int PADDING = 5;

    public static final Color UI_COLOUR = Color.LIGHT_GRAY;
    public static final Color TEXT_COLOUR = Color.BLACK;

    private Game game;

    private Stacker mainFrame;
    
    private Configuration settingsPanel;

    private Map<String, BufferedImage> cellMap;

    private static final int BOARD_MAX_HEIGHT = 641;
    private static final int BOARD_MIN_HEIGHT = 250;
    private int boardHeight;
    private int boardWidth;
    private int boardX;
    private int boardY;
    private int boardFallDistance;
    private int boardFallSpeed;
    private int boardBackX;

    private int sideDisplayWidth;
    private int holdX;
    private int nextX;

    private int cellSize;

    // EFFECTS: sets up the board panel
    public Board(Game game, Stacker mainFrame) {
        this.game = game;
        this.mainFrame = mainFrame;

        setLayout(null);

        settingsPanel = new Configuration(game, this);

        resetBoardFall();

        initializeCellMap();
    }

    // EFFECTS: get main frame
    public Stacker getMainFrame() {
        return mainFrame;
    }

    public int getBoardFallDistance() {
        return boardFallDistance;
    }

    // MODIFIES: this
    // EFFECTS: increments boardFallDistance by boardFallSpeed, increments boardFallSpeed by one,
    //          then repaints the board.
    //          this method is for use in a timer, which will cause an effect of the board falling off
    //          the screen.
    //          ensures the board will not keep falling if it has already fallen 2x the screen height
    //          (to save a liiiiittle resources)
    public void boardFall() {
        if (boardFallDistance > getHeight() * 2) {
            return;
        }

        boardFallDistance += boardFallSpeed;
        boardFallSpeed++;
        repaint();
    }

    // MODIFIES: this
    // EFFECTS: sets boardFallDistance back to 0
    public void resetBoardFall() {
        boardFallDistance = 0;
        boardFallSpeed = 0;
    }

    public Configuration getSettingsPanel() {
        return settingsPanel;
    }

    @Override
	protected void paintComponent(Graphics g) { 
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        
        drawBoard(g2);

        sideDisplayWidth = cellSize * 4 + PADDING * 2;
        g.setFont(new Font(FONT, 2, sideDisplayWidth / 7));
        drawHold(g2);
        drawNextAndScore(g2);

        settingsPanel.paintComponent(g2);
    }

    // MODIFIES: this
    // EFFECTS: maps all cells from data/texture/cell_default.png to their respective piece
    private void initializeCellMap() {
        cellMap = new HashMap<>();

        chooseCellTexture(DEFAULT_CELL_TEXTURE);
    }

    public void showAlert(String message) {
        mainFrame.showAlert(message);
    }

    // MODIFIES: this
    // EFFECTS: changes cell textures to those from a file in data/textures/
    public void chooseCellTexture(String fileName) {
        try {
            BufferedImage texture = ImageIO.read(new File("./data/textures/" + fileName));
            cellMap.put("Z", texture.getSubimage(0, 0, 30, 30));
            cellMap.put("L", texture.getSubimage(31, 0, 30, 30));
            cellMap.put("S", texture.getSubimage(62, 0, 30, 30));
            cellMap.put("O", texture.getSubimage(93, 0, 30, 30));
            cellMap.put("I", texture.getSubimage(124, 0, 30, 30));
            cellMap.put("J", texture.getSubimage(155, 0, 30, 30));
            cellMap.put("T", texture.getSubimage(186, 0, 30, 30));
            cellMap.put("Outline", texture.getSubimage(217, 0, 30, 30));
        } catch (IOException e) {
            showAlert("ALERT: Something went wrong while loading the texture. " + e.getMessage());
        }
    }

    /*****************************
     * MAIN BOARD RENDERING
     *****************************/

    // MODIFIES: this
    // EFFECTS: draws the board, dynamically accounting for the screen size
    //          EVERY element's size is dependant on the boardHeight in some shape or form
    private void drawBoard(Graphics2D g) {
        boardHeight = getHeight() - MARGIN * 8;
        boardHeight = Math.min(boardHeight, BOARD_MAX_HEIGHT);
        boardHeight = Math.max(boardHeight, BOARD_MIN_HEIGHT);

        cellSize = (boardHeight - 2 * PADDING) / 20;
        boardHeight = cellSize * 20 + PADDING * 2;

        boardWidth = cellSize * 10 + PADDING * 2 - 1;
        boardX = (getWidth() - boardWidth) / 2;
        boardY = MARGIN + cellSize * 3 + boardFallDistance;

        boardBackX = boardX + PADDING;

        g.setColor(UI_COLOUR);
        g.drawRect(boardX, boardY, boardWidth, boardHeight);

        g.fill(new Rectangle2D.Double(boardBackX, boardY + PADDING, cellSize * 10, cellSize * 20));

        g.setColor(getBackground());
        g.drawLine(boardX + 1, boardY, boardX + boardWidth - 1, boardY);
        

        // CELL RENDERING TEST, UNCOMMENT TO SEE EACH CELL
        // g.drawImage(cellMap.get("Z").getScaledInstance(cellSize, cellSize, ABORT), 20, 20, null);
        // g.drawImage(cellMap.get("L").getScaledInstance(cellSize, cellSize, ABORT), 20, 50, null);
        // g.drawImage(cellMap.get("S").getScaledInstance(cellSize, cellSize, ABORT), 20, 80, null);
        // g.drawImage(cellMap.get("O").getScaledInstance(cellSize, cellSize, ABORT), 20, 110, null);
        // g.drawImage(cellMap.get("I").getScaledInstance(cellSize, cellSize, ABORT), 20, 140, null);
        // g.drawImage(cellMap.get("J").getScaledInstance(cellSize, cellSize, ABORT), 20, 170, null);
        // g.drawImage(cellMap.get("T").getScaledInstance(cellSize, cellSize, ABORT), 20, 200, null);
        // g.drawImage(cellMap.get("Outline").getScaledInstance(cellSize, cellSize, ABORT), 20, 230, null);
        
        drawCells(g);
    }

    // MODIFIES: this
    // EFFECTS: renders specifically the cells on the board
    private void drawCells(Graphics2D g) {
        String[][] board = game.getBoard();
        Piece currPiece = game.getCurrentPiece();

        game.updatePiecePreview();

        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                drawCell(g, board, currPiece, y, x);

                // make sure opacity is set back to normal
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            }
        }
    }

    private void drawCell(Graphics2D g, String[][] board, Piece currPiece, int y, int x) {
        String cellType = board[y][x];
        Seg[] segments = currPiece.getConfiguration();
        
        boolean pieceIsHere = isPieceHere(segments, currPiece.getCenterX(), currPiece.getCenterY(), x, y);
        boolean previewIsHere = isPieceHere(segments, currPiece.getCenterX(), game.getDroppedY(), x, y);

        if (pieceIsHere) {
            cellType = currPiece.toString();
        } else if (previewIsHere) {
            cellType = "Outline";
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        }

        int posX = boardBackX + x * cellSize;
        int posY = (boardY + PADDING - cellSize * 3) + y * cellSize;

        if (cellType == null) {
            if (y >= 3) {
                g.setColor(Color.WHITE);
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
                g.drawRect(posX, posY, cellSize, cellSize);
            }
            return;
        }

        Image cellGraphic = cellMap.get(cellType).getScaledInstance(cellSize, cellSize, ABORT);
        g.drawImage(cellGraphic, posX, posY, null);
    }

    // EFFECTS: returns true if a segment is at (x, y)
    private boolean isPieceHere(Seg[] segments, int pieceX, int pieceY, int x, int y) {
        for (Seg seg : segments) {
            int posX = pieceX + seg.getX();
            int posY = pieceY + seg.getY();

            if (posX == x && posY == y) {
                return true;
            }
        }
        return false;
    }

    /*****************************
     * SIDE UI RENDERING
     *****************************/

    private void drawPieceInUI(Graphics2D g, String type, int startX, int verticalLevel) {
        Piece piece = game.getPieceByString(type);

        if (piece == null) {
            return;
        }

        int centerX = startX + PADDING + (cellSize * 3) / 2 + 1;
        int centerY = boardY + cellSize * (3 * verticalLevel + 2);

        if (type.equals("I") || type.equals("O")) {
            centerX = startX + PADDING + cellSize;
        }

        for (Seg seg : piece.getConfiguration()) {
            int posX = centerX + cellSize * seg.getX();
            int posY = centerY + cellSize * seg.getY();

            Image cellGraphic = cellMap.get(type).getScaledInstance(cellSize, cellSize, ABORT);
            g.drawImage(cellGraphic, posX, posY, null);
        }
    }

    /*****************************
     * HOLD PIECE
     */

    // EFFECTS: draws the ui that displays the current held piece
    private void drawHold(Graphics2D g) {
        holdX = boardX - (sideDisplayWidth + PADDING);
        int holdHeight = cellSize * 3 + PADDING * 2;

        g.setColor(UI_COLOUR);
        g.drawRect(holdX, boardY, sideDisplayWidth, holdHeight);

        g.setColor(TEXT_COLOUR);
        g.drawString("HOLD", holdX + PADDING, boardY + PADDING);

        drawPieceInUI(g, game.getHeldPiece(), holdX, 0);
    }
    
    /*****************************
     * NEXT PIECES AND SCORE DISPLAY
     */

    // EFFECTS: draws the ui that displays the next 5 pieces
    private void drawNextAndScore(Graphics2D g) {
        nextX = boardX + (boardWidth + PADDING);
        int nextHeight = (cellSize * 3) * 5 + PADDING * 2;

        g.setColor(UI_COLOUR);
        g.drawRect(nextX, boardY, sideDisplayWidth, nextHeight);

        g.setColor(TEXT_COLOUR);
        g.drawString("NEXT", nextX + PADDING, boardY + PADDING);

        List<String> bag = game.getBag();
        for (int i = 0; i < 5; i++) {
            drawPieceInUI(g, bag.get(i), nextX, i);
        }

        g.drawString("SCORE: " + game.getScore(), nextX + PADDING, boardY + nextHeight + PADDING * 3);
    }
}
