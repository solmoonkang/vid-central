package com.vidcentral.api.infrastructure.converter;

import org.springframework.stereotype.Component;

import com.vidcentral.global.common.util.AESGcmUtil;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Component
@Converter(autoApply = true)
public class AESGcmAttributeConverter implements AttributeConverter<String, String> {

	private final AESGcmUtil aesGcmUtil;

	public AESGcmAttributeConverter(AESGcmUtil aesGcmUtil) {
		this.aesGcmUtil = aesGcmUtil;
	}

	@Override
	public String convertToDatabaseColumn(String attribute) {
		if (attribute == null) return null;
		return aesGcmUtil.encrypt(attribute);
	}

	@Override
	public String convertToEntityAttribute(String dbData) {
		if (dbData == null) return null;
		return aesGcmUtil.decrypt(dbData);
	}
}
