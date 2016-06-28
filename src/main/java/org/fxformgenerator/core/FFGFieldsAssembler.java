package org.fxformgenerator.core;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * Methods to assembly form fields
 * into single/multiple columns
 *
 * Created by giovanni on 4/13/16.
 */
public class FFGFieldsAssembler {
    private int formLayout = FFGLayout.MULTIPLE_COLUMNS;

    /** If > 0, Layout will be forced to use this number of columns  */
    private int numberOfColumns = -1;

    /**
     * Assembly all form nodes into a single VBox parent
     * @return A VBox containing each row from forms
     */
    protected VBox assembly(List<FFGInputGroup> inputGroups) {
        switch (this.formLayout) {
            case FFGLayout.MULTIPLE_COLUMNS:
                return this.assemblyMultipleColumns(inputGroups);

            case FFGLayout.MULTIPLE_FULLWIDTHROWS_COLUMNS:
                return this.assemblyMultipleColumnsRowsFilled(inputGroups);

            case FFGLayout.SINGLE_COLUMN:
            default:
                return this.assemblySingleColumn(inputGroups);
        }
    }

    public void assignFormLayout(int formLayout) {
        this.formLayout = formLayout;
    }

    private VBox assemblyMultipleColumns(List<FFGInputGroup> inputGroups) {
        VBox formContainer = new VBox(3);
        int numColumns = this.determineColumnsNumber(inputGroups);

        for (int i = 0; i < inputGroups.size(); i+=numColumns) {

            HBox formRow;
            if (i + (numColumns - 1) < inputGroups.size()) {
                formRow = this.putInRow(inputGroups.subList(i, i + numColumns));
            }
            else {
                formRow = this.putInRow(inputGroups.subList(i, inputGroups.size()));
            }

            formContainer.getChildren().add(formRow);
        }

        return formContainer;
    }

    private VBox assemblyMultipleColumnsRowsFilled(List<FFGInputGroup> inputGroups) {
        VBox formContainer = new VBox(3);
        int numColumns = this.determineColumnsNumber(inputGroups);

        for (int i = 0; i < inputGroups.size(); i += numColumns) {

            HBox formRow;
            if (i + (numColumns - 1) < inputGroups.size()) {
                formRow = this.putInRow(inputGroups.subList(i, i + numColumns));
            }
            else {
                List<FFGInputGroup> rowInputGroups = inputGroups.subList(i, inputGroups.size());
                double rowWidth = (rowInputGroups.get(0).getMinMaxEditorWidth() * numColumns) + (10 * (numColumns - 1));
                double rowWidthWithoutSpacing = rowWidth - ((rowInputGroups.size() - 1) * 10);
                double adaptedColumnWidth = rowWidthWithoutSpacing / rowInputGroups.size();

                rowInputGroups.stream().forEach(inputGroup -> inputGroup.setMinMaxEditorWidth(adaptedColumnWidth));
                formRow = this.putInRow(inputGroups.subList(i, inputGroups.size()));
            }

            formContainer.getChildren().add(formRow);
        }

        return formContainer;
    }

    private VBox assemblySingleColumn(List<FFGInputGroup> inputGroups) {
        VBox formContainer = new VBox(3);
        for (FFGInputGroup inputGroup : inputGroups) {
            formContainer.getChildren().add(inputGroup.buildAsVBox());
        }

        return formContainer;
    }

    private int determineColumnsNumber(List<FFGInputGroup> inputGroups) {
        if (numberOfColumns > 0) {
            return numberOfColumns;
        }
        else if (inputGroups.size() <= 6) {
            return 2;
        }
        else if (inputGroups.size() <= 12) {
            return 3;
        }
        else {
            return 4;
        }
    }

    private HBox putInRow(List<FFGInputGroup> inputGroups) {
        HBox hBox = new HBox(10);

        inputGroups.stream().forEach(inputGroup -> {
            hBox.getChildren().add(inputGroup.buildAsVBox());
        });

        return hBox;
    }

    /**
     * Assigns the number of columns to use in form layout
     * @param numberOfColumns If > 0, Layout will be forced to use this
     *                        number of columns
     * @return
     */
    public void setNumberOfColumns(int numberOfColumns) {
        this.numberOfColumns = numberOfColumns;
    }
}
