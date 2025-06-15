package com.gyp.common.intefaces;

import java.util.List;

/**
 * BaseMapper interface defines the contract for mapping between request DTOs,
 * response DTOs, and entity objects. It provides methods to convert between
 * these types, allowing for a consistent approach to data transformation.
 *
 * @param <T>
 * 		the type of the request DTO
 * @param <R>
 * 		the type of the response DTO
 * @param <E>
 * 		the type of the entity
 */
public interface BaseMapper<T, R, E> {

	/**
	 * Converts an entity to a response DTO.
	 *
	 * @param entity
	 * 		the entity to convert
	 * @return the converted response DTO
	 */
	R toResponseDto(E entity);

	/**
	 * Converts a request DTO to an entity.
	 *
	 * @param requestDto
	 * 		the request DTO to convert
	 * @return the converted entity
	 */
	E toEntity(T requestDto);

	/**
	 * Updates an existing entity from a request DTO.
	 *
	 * @param entities
	 * 		the request DTO to update the entity with
	 */
	List<R> toResponseDtoList(List<E> entities);
}
