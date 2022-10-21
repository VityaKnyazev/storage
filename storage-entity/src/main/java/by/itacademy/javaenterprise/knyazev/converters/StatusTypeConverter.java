package by.itacademy.javaenterprise.knyazev.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import by.itacademy.javaenterprise.knyazev.utils.Status;

@Converter
public class StatusTypeConverter implements AttributeConverter<Status, String> {

	@Override
	public String convertToDatabaseColumn(Status status) {
		if (status == null) {
			return null;
		}
		
		switch (status) {
		case reserved :
			return Status.reserved.name();
		case bought :
			return Status.bought.name();
		case deleted :
			return Status.deleted.name();
		default:
			throw new IllegalArgumentException(status.name() + " not supported.");
		}		
		
	}

	@Override
	public Status convertToEntityAttribute(String dbstatus) {
		if (dbstatus == null) {
			return null;
		}
		
		if (dbstatus.equals(Status.reserved.name())) {
			return Status.reserved;
		} else if (dbstatus.equals(Status.bought.name())) {
			return Status.bought;
		}  else if (dbstatus.equals(Status.deleted.name())) {
			return Status.deleted;
		} else {
			throw new IllegalArgumentException(dbstatus + " not supported.");
		}	
	}

}
