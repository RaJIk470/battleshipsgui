package org.example.field;

import lombok.Data;
import org.example.battleship.Battleship;
import org.example.battleship.BattleshipPart;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

@Data
public class BattleshipField extends JPanel {
    private int width;
    private int height;
    private int cellWidth;
    private int cellHeight;

    private int fieldLength;
    private ArrayList<Battleship> battleships;

    private CellType[][] field;

    private boolean isFilled = false;

    public BattleshipField(int width, int height) {
        this.battleships = new ArrayList<>();
        this.width = width + 1;
        this.height = height + 1;
        cellWidth = width / 10;
        cellHeight = height / 10;
        fieldLength = 10;
        field = new CellType[10][10];
        clearField();
        setPreferredSize(new Dimension(this.width, this.height));
    }

    public void clearField() {
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                field[i][j] = CellType.EMPTY;
            }
        }
        battleships = new ArrayList<>();
        isFilled = false;
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        g.setColor(new Color(147, 191, 207));
        for (int i = 0; i < width; i += cellWidth) {
            for (int j = 0; j < height; j += cellHeight) {
                g2d.setStroke(new BasicStroke(2));
                g2d.fill(new Rectangle2D.Float(i, j, cellWidth - 2, cellHeight - 2));
                //g.drawRect(i, j, cellWidth, cellHeight);
            }
        }

        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                switch (field[i][j]) {
                    case EMPTY -> {
                    }
                    case SHOT -> {
                        drawCross(g2d, i, j);
                    }
                    case BATTLESHIP -> {
                        g.setColor(new Color(96, 150, 180));
                        //g.fillRect(i * cellWidth, j * cellHeight, cellWidth, cellHeight);
                        g2d.fill(new Rectangle2D.Float(i * cellWidth, j * cellHeight, cellWidth - 2, cellHeight - 2));
                    }
                    case DEAD -> {
                        g.setColor(new Color(96, 150, 180));
                        //g.fillRect(i * cellWidth, j * cellHeight, cellWidth, cellHeight);
                        g2d.fill(new Rectangle2D.Float(i * cellWidth , j * cellHeight, cellWidth - 2, cellHeight - 2));
                        drawCross(g2d, i, j);
                    }
                }
            }
        }
        repaint();
    }

    private void drawCross(Graphics2D g, int i, int j) {
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(2));
        g.draw(new Line2D.Float(i * cellWidth + 4, j * cellHeight + 4, (i + 1) * cellWidth - 4, (j + 1) * cellHeight - 4));
        g.draw(new Line2D.Float((i + 1) * cellWidth - 4 , j * cellHeight + 4, i * cellWidth + 4, (j + 1) * cellHeight - 4));
    }

    private void drawPlus(Graphics2D g, int i, int j) {
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(2));
        g.draw(new Line2D.Float(i * cellWidth + 4, j * cellHeight + cellHeight / 2, i * cellWidth + cellWidth - 4, j * cellHeight + cellHeight / 2));
        g.draw(new Line2D.Float(i * cellWidth + cellWidth / 2, j * cellHeight + 4, i * cellWidth + cellWidth / 2 , j * cellHeight + cellHeight - 4));
    }

    public void setBattleships(List<Battleship> battleships) {
        this.battleships = new ArrayList<>(battleships);
        for (Battleship bs : battleships) {
            for (BattleshipPart bp : bs.getParts()) {
                field[bp.getPosition().x][bp.getPosition().y] = CellType.BATTLESHIP;
            }
        }
        repaint();
    }

    public CellType getCellAt(Point point) {
        return field[point.x][point.y];
    }

    public void setCell(Point point, CellType cellType) {
        field[point.x][point.y] = cellType;
        repaint();
    }

    public Optional<Battleship> getBattleshipAt(Point point) {
        for (Battleship bs : battleships) {
            for (BattleshipPart bp : bs.getParts()) {
                if (bp.getPosition().equals(point)) {
                    return Optional.of(bs);
                }
            }
        }

        return Optional.empty();
    }

    public void addBattleship(Point point, int length, boolean isVertical) {
        List<BattleshipPart> parts = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            Point partPoint = new Point(point);
            if (isVertical) {
                partPoint.translate(0, i);
                parts.add(new BattleshipPart(partPoint));
            } else {
                partPoint.translate(i, 0);
                parts.add(new BattleshipPart(partPoint));
            }
        }

        Battleship battleship = new Battleship(parts);
        battleships.add(battleship);
        for (BattleshipPart bp : battleship.getParts()) {
            field[bp.getPosition().x][bp.getPosition().y] = CellType.BATTLESHIP;
        }
        repaint();
    }

    public void addBattleship(Battleship battleship) {
        battleships.add(battleship);
        repaint();
    }

    public Optional<Battleship> removeBattleship(Point point) {
        Optional<Battleship> optionalBattleship = getBattleshipAt(point);
        if (optionalBattleship.isPresent()) {
            Battleship battleship = optionalBattleship.get();
            for (BattleshipPart bp : battleship.getParts()) {
                field[bp.getPosition().x][bp.getPosition().y] = CellType.EMPTY;
                System.out.println(Arrays.deepToString(field));
            }
            battleships.remove(battleship);

            repaint();
            return Optional.of(battleship);
        }

        return Optional.empty();
    }

    public boolean isAllDead() {
        return battleships.stream().allMatch(Battleship::isDead);
    }
}
