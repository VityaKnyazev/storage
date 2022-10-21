package by.itacademy.javaenterprise.knyazev.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import by.itacademy.javaenterprise.knyazev.utils.RoleType;

@Converter
public class RoleTypeConverter implements AttributeConverter<RoleType, String> {

	@Override
	public String convertToDatabaseColumn(RoleType roleType) {
		if (roleType == null) {
			return null;
		}
		
		switch (roleType) {
		case ROLE_ADMIN :
			return RoleType.ROLE_ADMIN.name();
		case ROLE_USER :
			return RoleType.ROLE_USER.name();
		default:
			throw new IllegalArgumentException(roleType.name() + " not supported.");
		}		
		
	}

	@Override
	public RoleType convertToEntityAttribute(String dbRoleType) {
		if (dbRoleType == null) {
			return null;
		}
		
		if (dbRoleType.equals(RoleType.ROLE_ADMIN.name())) {
			return RoleType.ROLE_ADMIN;
		} else if (dbRoleType.equals(RoleType.ROLE_USER.name())) {
			return RoleType.ROLE_USER;
		} else {
			throw new IllegalArgumentException(dbRoleType + " not supported.");
		}	
	}

}
