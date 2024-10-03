package com.writeitup.wiu_user_service.util;

import com.writeitup.wiu_user_service.domain.User;
import com.writeitup.wiu_user_service.dto.UserDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {

    @Mapping(source = "imageData", target = "imageData", qualifiedByName = "byteArrayToString")
    UserDTO toUserDTO(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "imageData", target = "imageData", qualifiedByName = "stringToByteArray")
    void updateUser(UserDTO userDTO, @MappingTarget User user);

    @Named("byteArrayToString")
    static String byteArrayToString(final byte[] imageData) {
        return new String(imageData);
    }

    @Named("stringToByteArray")
    static byte[] stringToByteArray(final String imageData) {
        return imageData.getBytes();
    }
}
