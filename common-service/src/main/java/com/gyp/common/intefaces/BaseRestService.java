package com.gyp.common.intefaces;

/**
 * BaseService interface defines the contract for service between request DTOs,
 * response DTOs, and entity objects. It provides methods to query data from repository
 * between these types, allowing for a consistent approach to data transformation.
 *
 * @param <T>
 * 		the type of the request DTO
 * @param <R>
 * 		the type of the response DTO
 */
public interface BaseRestService<T, R> {

	/**
	 * Retrieves a response DTO by its ID.
	 *
	 * @param id
	 * 		the ID of the entity to retrieve
	 * @return the response DTO corresponding to the given ID
	 */
	R getById(String id);

	/**
	 * Retrieves a response DTO by its name.
	 *
	 * @param name
	 * 		the name of the entity to retrieve
	 * @return the response DTO corresponding to the given name
	 */
	R getByName(String name);

	/**
	 * Retrieves all response DTOs.
	 *
	 * @return a list of all response DTOs
	 */
	R create(T requestDto);

	/**
	 * Creates a new entity from the request DTO.
	 *
	 * @param requestDto
	 * 		the request DTO to create the entity from
	 * @return the created response DTO
	 */
	R update(String id, T requestDto);

	/**
	 * Updates an existing entity from the request DTO.
	 *
	 * @param id
	 * 		the ID of the entity to update
	 */
	void delete(String id);
}
