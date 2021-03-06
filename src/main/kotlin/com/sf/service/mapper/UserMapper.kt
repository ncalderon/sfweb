package com.sf.service.mapper

import com.sf.domain.Authority
import com.sf.domain.UserEntity
import com.sf.service.dto.UserDTO

import org.springframework.stereotype.Service

/**
 * Mapper for the entity [UserEntity] and its DTO called [UserDTO].
 *
 * Normal mappers are generated using MapStruct, this one is hand-coded as MapStruct
 * support is still in beta, and requires a manual step with an IDE.
 */
@Service
class UserMapper {

    fun usersToUserDTOs(users: List<UserEntity?>): MutableList<UserDTO> {
        return users.asSequence()
            .filterNotNull()
            .mapTo(mutableListOf()) { this.userToUserDTO(it) }
    }

    fun userToUserDTO(user: UserEntity): UserDTO {
        return UserDTO(user)
    }

    fun userDTOsToUsers(userDTOs: List<UserDTO?>): MutableList<UserEntity> {
        return userDTOs.asSequence()
            .map { userDTOToUser(it) }
            .filterNotNullTo(mutableListOf())
    }

    fun userDTOToUser(userDTO: UserDTO?): UserEntity? {
        return when (userDTO) {
            null -> null
            else -> {
                UserEntity(
                    id = userDTO.id,
                    login = userDTO.login,
                    firstName = userDTO.firstName,
                    lastName = userDTO.lastName,
                    email = userDTO.email,
                    imageUrl = userDTO.imageUrl,
                    activated = userDTO.activated,
                    langKey = userDTO.langKey,
                    authorities = authoritiesFromStrings(userDTO.authorities)
                )
            }
        }
    }

    private fun authoritiesFromStrings(authoritiesAsString: Set<String>?): MutableSet<Authority> {
        return authoritiesAsString?.mapTo(mutableSetOf()) { Authority(name = it) } ?: mutableSetOf()
    }

    fun userFromId(id: Long?): UserEntity? {
        return id?.let { UserEntity(id = it) }
    }
}
