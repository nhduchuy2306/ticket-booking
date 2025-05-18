package com.gyp.common.services;

import java.util.NoSuchElementException;
import java.util.function.Supplier;

import jakarta.validation.ConstraintViolationException;

import com.gyp.common.exceptions.DataIntegrityException;
import com.gyp.common.exceptions.DatabaseException;
import com.gyp.common.exceptions.DuplicateResourceException;
import com.gyp.common.exceptions.ResourceNotFoundException;
import com.gyp.common.exceptions.SystemException;
import com.gyp.common.exceptions.TransactionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.TransactionSystemException;

public abstract class AbstractService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * Executes a repository operation wrapped in exception handling.
	 *
	 * @param operation
	 * 		The repository operation to execute (represented as a Supplier)
	 * @param errorMessage
	 * 		Custom error message to include in exceptions
	 * @param <T>
	 * 		The return type of the operation
	 * @return The result of the operation
	 *
	 * @throws BusinessException
	 * 		when database errors occur, converted to appropriate business exceptions
	 */
	protected <T> T callWithException(Supplier<T> method, String errorMessage) {
		try {
			return method.get();
		} catch(EmptyResultDataAccessException | NoSuchElementException e) {
			logger.warn("Resource not found: {}", errorMessage, e);
			throw new ResourceNotFoundException(errorMessage, e);
		} catch(DuplicateKeyException e) {
			logger.error("Duplicate key violation: {}", errorMessage, e);
			throw new DuplicateResourceException(errorMessage, e);
		} catch(DataIntegrityViolationException | ConstraintViolationException e) {
			logger.error("Data integrity violation: {}", errorMessage, e);
			throw new DataIntegrityException(errorMessage, e);
		} catch(TransactionSystemException e) {
			logger.error("Transaction failed: {}", errorMessage, e);
			throw new TransactionException("Failed to complete database transaction", e);
		} catch(DataAccessException e) {
			logger.error("Database access error: {}", errorMessage, e);
			throw new DatabaseException(errorMessage, e);
		} catch(Exception e) {
			logger.error("Unexpected error during database operation: {}", errorMessage, e);
			throw new SystemException("An unexpected error occurred. Please try again later.", e);
		}
	}
}
