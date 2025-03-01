package sheetmanager.expression.impl.math;


import sheetmanager.expression.Expression;
import sheetmanager.expression.impl.UnaryExpression;
import sheetmanager.sheet.SheetDataRetriever;
import sheetmanager.sheet.coordinate.Coordinate;
import sheetmanager.sheet.effectivevalue.CellType;
import sheetmanager.sheet.effectivevalue.EffectiveValue;
import sheetmanager.sheet.effectivevalue.EffectiveValueImpl;
import sheetmanager.sheet.range.RangeReadActions;

import java.util.List;
import java.util.Objects;

public class Average extends UnaryExpression {

    private final SheetDataRetriever sheet;
    private Coordinate targetCoordinate;

    public Average(Expression expression, SheetDataRetriever sheet, Coordinate targetCoordinate) {
        super(expression);
        this.sheet = sheet;
        this.targetCoordinate = targetCoordinate;
    }

    @Override
    protected EffectiveValue doEvaluate(EffectiveValue value) {
        EffectiveValue result;
        if (!isValid(value))
        {
            result = new EffectiveValueImpl(CellType.NUMERIC, Double.NaN);
        }
        else {
            String rangeName = value.extractValueWithExpectation(String.class);
            RangeReadActions range = sheet.getRangeReadActions(rangeName);
            if (range == null){
                result = new EffectiveValueImpl(CellType.NUMERIC, Double.NaN);
            }
            else{
                double sum = 0;
                sheet.setCellDependentOnRange(targetCoordinate, rangeName);
                List<Coordinate> coordintes = range.getCoordinates();
                int numOfCells = 0;
                for (Coordinate coordinate : coordintes) {
                    sheet.addDependentCell(this.targetCoordinate, coordinate);
                    EffectiveValue cellEffectiveValue = sheet.getCellEffectiveValue(coordinate);
                    if (cellEffectiveValue.getCellType() == CellType.NUMERIC
                            && !Double.isNaN(cellEffectiveValue.extractValueWithExpectation(Double.class))){
                        sum+= cellEffectiveValue.extractValueWithExpectation(Double.class);
                        numOfCells++;
                    }
                }
                if (numOfCells == 0){
                    result = new EffectiveValueImpl(CellType.NUMERIC, Double.NaN);
                }
                else {
                    result = new EffectiveValueImpl(CellType.NUMERIC, sum / numOfCells);
                }
            }
        }
        return result;
    }

    @Override
    protected boolean isValid(EffectiveValue value) {
        return (value.getCellType() == CellType.STRING && !Objects.equals(value.extractValueWithExpectation(String.class), "!UNDEFINED!"));
    }

}
