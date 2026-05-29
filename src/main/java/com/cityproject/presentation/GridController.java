package com.cityproject.presentation;

import com.cityproject.model.Cell;
import com.cityproject.model.CityState;
import com.cityproject.model.Infrastructure;
import com.cityproject.model.factory.BuildingFactory;
import com.cityproject.presentation.ControlsController.VisionMode;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.control.ScrollPane;
import javafx.scene.Group;
import javafx.scene.control.Slider;

import javafx.scene.layout.VBox;
import javafx.scene.input.ScrollEvent;
import javafx.animation.FadeTransition;
import javafx.util.Duration;

public class GridController {

    @FXML private GridPane cityGrid;
    @FXML private ScrollPane scrollPane;
    @FXML private Group zoomGroup;
    @FXML private Slider zoomSlider;
    @FXML private VBox zoomContainer;

    private static final int CELL_SIZE = 28;
    private static final int GRID_ROWS = 30;
    private static final int GRID_COLS = 30;

    private CityState cityState;
    private ControlsController controlsController;
    private MainController mainController;

    public void init(CityState cityState, ControlsController controlsController, MainController mainController) {
        this.cityState = cityState;
        this.controlsController = controlsController;
        this.mainController = mainController;
        
        setupZoomAndPan();
        buildGrid();
    }

    private void setupZoomAndPan() {
        // Bind slider value to GridPane scale (Group will auto-resize its layout bounds to fit it)
        cityGrid.scaleXProperty().bind(zoomSlider.valueProperty());
        cityGrid.scaleYProperty().bind(zoomSlider.valueProperty());

        // Handle mouse wheel scrolling for zoom (CTRL + Scroll) and horizontal pan (SHIFT + Scroll)
        scrollPane.addEventFilter(ScrollEvent.SCROLL, event -> {
            if (event.isControlDown() && event.getDeltaY() != 0) {
                event.consume();
                double zoomFactor = 1.05;
                if (event.getDeltaY() < 0) {
                    zoomFactor = 1 / zoomFactor;
                }
                double newZoom = zoomSlider.getValue() * zoomFactor;
                // Clamping between min and max
                if (newZoom < zoomSlider.getMin()) newZoom = zoomSlider.getMin();
                if (newZoom > zoomSlider.getMax()) newZoom = zoomSlider.getMax();
                zoomSlider.setValue(newZoom);
            } else if (event.isShiftDown() && event.getDeltaY() != 0) {
                event.consume();
                double delta = event.getDeltaY();
                // hvalue goes from 0.0 to 1.0. We adjust it based on scroll speed.
                double width = scrollPane.getContent().getBoundsInLocal().getWidth();
                double viewWidth = scrollPane.getViewportBounds().getWidth();
                if (width > viewWidth) {
                    double step = 40.0 / (width - viewWidth); // 40px per scroll click approx
                    if (delta > 0) {
                        scrollPane.setHvalue(Math.max(0, scrollPane.getHvalue() - step));
                    } else {
                        scrollPane.setHvalue(Math.min(1, scrollPane.getHvalue() + step));
                    }
                }
            }
        });

        // Hover effect for the zoom slider container
        zoomContainer.setOnMouseEntered(e -> {
            FadeTransition ft = new FadeTransition(Duration.millis(200), zoomContainer);
            ft.setToValue(1.0);
            ft.play();
        });
        zoomContainer.setOnMouseExited(e -> {
            FadeTransition ft = new FadeTransition(Duration.millis(200), zoomContainer);
            ft.setToValue(0.3);
            ft.play();
        });
        
        // Remove scrollbars wrapping so they appear when zoomed
        // (fitToWidth/fitToHeight are removed in FXML)
    }

    private void buildGrid() {
        cityGrid.getChildren().clear();
        for (int row = 0; row < GRID_ROWS; row++) {
            for (int col = 0; col < GRID_COLS; col++) {
                Pane cell = createCellPane(row, col);
                cityGrid.add(cell, col, row);
            }
        }
    }

    private Pane createCellPane(int row, int col) {
        StackPane pane = new StackPane();
        pane.setPrefSize(CELL_SIZE, CELL_SIZE);
        pane.setStyle(getCellStyle(row, col));

        // pollution value label (centered)
        Label pollutionLabel = new Label();
        pollutionLabel.setId("pollutionLabel");
        pollutionLabel.setStyle("-fx-text-fill: white; -fx-font-size:10; -fx-font-weight:bold;");
        pollutionLabel.setMouseTransparent(true);
        pane.getChildren().add(pollutionLabel);

        // population stats label (centered)
        Label populationStatsLabel = new Label();
        populationStatsLabel.setId("populationStatsLabel");
        populationStatsLabel.setStyle("-fx-text-fill: white; -fx-font-size:10; -fx-font-weight:bold; -fx-alignment: center; -fx-text-alignment: center;");
        populationStatsLabel.setMouseTransparent(true);
        pane.getChildren().add(populationStatsLabel);

        Tooltip tip = new Tooltip();
        pane.setOnMouseEntered(e -> {
            Cell c = cityState.getCell(row, col);
            tip.setText(getCellTooltip(c));
            Tooltip.install(pane, tip);
        });

        pane.setOnMouseClicked(e -> handleCellClick(row, col));

        return pane;
    }

    private String getCellStyle(int row, int col) {
        Cell cell = cityState.getCell(row, col);
        String color = getCellColor(cell);
        return "-fx-background-color:" + color + ";" +
               "-fx-border-color:#2d2d3d; -fx-border-width:0.5;";
    }

    private String getCellColor(Cell cell) {
        VisionMode currentVision = controlsController != null ? controlsController.getCurrentVision() : VisionMode.NORMAL;

        if (currentVision == VisionMode.POLLUTION) {
            int p = cell.getPollution();
            if (p == 0)   return "#13131f";
            if (p < 10)   return "#365314";
            if (p < 25)   return "#713f12";
            if (p < 50)   return "#7c2d12";
            return "#450a0a";
        }

        if (currentVision == VisionMode.FIRE) {
            return cell.hasFireProtection() ? "#166534" : "#13131f";
        }

        if (currentVision == VisionMode.ENTERTAINMENT) {
            int e = cell.getEntertainmentBonus();
            if (e == 0)   return "#13131f";
            if (e < 20)   return "#581c87"; 
            if (e < 50)   return "#7e22ce"; 
            if (e < 100)  return "#a855f7"; 
            return "#d8b4fe"; 
        }

        if (currentVision == VisionMode.LOCAL_HAPPINESS) {
            if (cell.isEmpty() || !cell.getStructure().hasComponent(com.cityproject.model.components.HousingComponent.class)) {
                return "#13131f";
            }
            com.cityproject.model.components.HousingComponent hc = cell.getStructure().getComponent(com.cityproject.model.components.HousingComponent.class);
            if (hc.getLocalHappiness() < 40) return "#7f1d1d"; 
            if (hc.getLocalHappiness() < 70) return "#92400e"; 
            return "#166534"; 
        }

        if (currentVision == VisionMode.LOCAL_HEALTH) {
            if (cell.isEmpty() || !cell.getStructure().hasComponent(com.cityproject.model.components.HousingComponent.class)) {
                return "#13131f";
            }
            com.cityproject.model.components.HousingComponent hc = cell.getStructure().getComponent(com.cityproject.model.components.HousingComponent.class);
            if (hc.getLocalHealth() < 40) return "#7f1d1d"; 
            if (hc.getLocalHealth() < 70) return "#b45309"; 
            return "#1d4ed8"; 
        }

        if (cell.isEmpty()) return "#13131f";

        Infrastructure s = cell.getStructure();
        if (!s.isActive()) return "#374151"; 

        return switch (s.getType().getId()) {
            case "ROAD", "ROOT_ROAD" -> "#6b7280";
            case "COTTAGE", "CONDO", "SKYSCRAPER" -> "#166534";
            case "FOOD", "METALLURGICAL", "PETROCHEMICAL" -> "#92400e";
            case "WIND", "SOLAR", "NUCLEAR" -> "#1e40af";
            case "COAL", "INCINERATOR", "OIL" -> "#7f1d1d";
            case "PARK", "NATURAL_RESERVE", "NATIONAL_PARK" -> "#14532d";
            case "CLINIC", "HOSPITAL" -> "#1e3a8a";
            case "FIRE_STATION" -> "#991b1b";
            case "CINEMA", "AMUSEMENT_PARK" -> "#9333ea";
            default -> "#4b5563";
        };
    }

    private String getCellTooltip(Cell cell) {
        if (cell.isEmpty()) return String.format("(%d,%d) Empty | Pollution: %d",
                cell.getX(), cell.getY(), cell.getPollution());
        Infrastructure s = cell.getStructure();
        return String.format("(%d,%d) %s\nActive: %s | Pollution: %d",
                cell.getX(), cell.getY(), s.getId(),
                s.isActive(), cell.getPollution());
    }

    private void handleCellClick(int row, int col) {
        if (controlsController == null) return;
        String selectedType = controlsController.getSelectedBuildingType();
        if (selectedType == null) return;

        Cell cell = cityState.getCell(row, col);

        if (selectedType.equals("DELETE")) {
            if (cell.isEmpty()) {
                controlsController.log("Cell (" + row + "," + col + ") is already empty.");
                return;
            }
            Infrastructure s = cell.getStructure();
            if (s != null && s.getType() != null && "ROOT_ROAD".equals(s.getType().getId())) {
                controlsController.log("Cannot delete the root road!");
                return;
            }
            cityState.removeBuilding(s);
            cell.setStructure(null);
            controlsController.log("Deleted " + s.getId() + " at (" + row + "," + col + ")");
            mainController.onCityUpdated(cityState);
            return;
        }

        if (!cell.isEmpty()) {
            controlsController.log("Cell (" + row + "," + col + ") is already occupied.");
            return;
        }

        Infrastructure preview = BuildingFactory.getInstance().createBuilding(selectedType, row, col);
        if (cityState.getBudget() < preview.getType().getBuildCost()) {
            controlsController.log("Not enough budget to build " + selectedType + "!");
            return;
        }

        cityState.setBudget(cityState.getBudget() - preview.getType().getBuildCost());
        cityState.addBuilding(preview);
        cell.setStructure(preview);

        controlsController.log("Built " + selectedType + " at (" + row + "," + col + ")");
        mainController.onCityUpdated(cityState);
    }

    public void refreshGrid() {
        VisionMode currentVision = controlsController != null ? controlsController.getCurrentVision() : VisionMode.NORMAL;

        for (javafx.scene.Node node : cityGrid.getChildren()) {
            Integer row = GridPane.getRowIndex(node);
            Integer col = GridPane.getColumnIndex(node);
            if (row == null || col == null) continue;
            if (node instanceof Pane pane) {
                pane.setStyle(getCellStyle(row, col));
                Cell cell = cityState.getCell(row, col);
                for (javafx.scene.Node child : pane.getChildren()) {
                    if (child instanceof Label lbl && "pollutionLabel".equals(lbl.getId())) {
                        if (currentVision == VisionMode.POLLUTION) {
                            lbl.setText(String.valueOf(cell.getPollution()));
                            lbl.setVisible(true);
                        } else {
                            lbl.setText("");
                            lbl.setVisible(false);
                        }
                    }
                    if (child instanceof Label lbl && "populationStatsLabel".equals(lbl.getId())) {
                        if (currentVision == VisionMode.LOCAL_HAPPINESS && !cell.isEmpty() && cell.getStructure().hasComponent(com.cityproject.model.components.HousingComponent.class)) {
                            com.cityproject.model.components.HousingComponent hc = cell.getStructure().getComponent(com.cityproject.model.components.HousingComponent.class);
                            lbl.setText(String.format("%d", (int)hc.getLocalHappiness()));
                            lbl.setVisible(true);
                        } else if (currentVision == VisionMode.LOCAL_HEALTH && !cell.isEmpty() && cell.getStructure().hasComponent(com.cityproject.model.components.HousingComponent.class)) {
                            com.cityproject.model.components.HousingComponent hc = cell.getStructure().getComponent(com.cityproject.model.components.HousingComponent.class);
                            lbl.setText(String.format("%d", (int)hc.getLocalHealth()));
                            lbl.setVisible(true);
                        } else {
                            lbl.setText("");
                            lbl.setVisible(false);
                        }
                    }
                }
            }
        }
    }
}
