/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package infrastructure;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 *
 * @author zymci
 */
@Converter(autoApply = true)
public class BooleanToNumberConverter implements AttributeConverter<Boolean, Integer> {
    @Override
    public Integer convertToDatabaseColumn(Boolean value) {
        if (value == null) {
            return 0;
        }
        return value ? 1 : 0;
    }

    @Override
    public Boolean convertToEntityAttribute(Integer dbData) {
        return dbData != null && dbData == 1;
    }
}
