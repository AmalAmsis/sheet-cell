package expression.impl.math;

import expression.Expression;
import expression.impl.UnaryExpression;
import sheet.SheetDataRetriever;
import sheet.coordinate.Coordinate;
import sheet.effectivevalue.CellType;
import sheet.effectivevalue.EffectiveValue;
import sheet.effectivevalue.EffectiveValueImpl;
import sheet.range.RangeReadActions;

import java.util.List;
import java.util.Objects;

public class Sum extends UnaryExpression {

    private final SheetDataRetriever sheet;
    private Coordinate targetCoordinate;

    public Sum(Expression expression, SheetDataRetriever sheet, Coordinate targetCoordinate) {
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
                List<Coordinate> coordintes = range.getCoordinates();
                for (Coordinate coordinate : coordintes) {
                    sheet.addDependentCell(this.targetCoordinate, coordinate);
                    EffectiveValue cellEffectiveValue = sheet.getCellEffectiveValue(coordinate);
                    if (cellEffectiveValue.getCellType() == CellType.NUMERIC
                        && !Double.isNaN(cellEffectiveValue.extractValueWithExpectation(Double.class))){
                        sum+= cellEffectiveValue.extractValueWithExpectation(Double.class);
                    }
                }
                result = new EffectiveValueImpl(CellType.NUMERIC, sum);
            }
        }
        return result;
    }

    @Override
    protected boolean isValid(EffectiveValue value) {
        return (value.getCellType() == CellType.STRING && !Objects.equals(value.extractValueWithExpectation(String.class), "!UNDEFINED!"));
    }
}
