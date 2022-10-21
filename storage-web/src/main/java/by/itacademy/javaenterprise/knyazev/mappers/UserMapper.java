package by.itacademy.javaenterprise.knyazev.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;

import by.itacademy.javaenterprise.knyazev.dto.UserDTO;
import by.itacademy.javaenterprise.knyazev.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

	@Mappings({
	@Mapping(target = "enabled", expression = "java(true)"),
	@Mapping(target = "id", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS),
	@Mapping(target = "name", source = "username"),
	@Mapping(target = "roles", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
	})
	public User toUser(UserDTO userDTO);
	
	
	@Mapping(target = "username", source = "name")
	public UserDTO toDTO(User user);
	
	public List<UserDTO> toDTO(List<User> user);
}