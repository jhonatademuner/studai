package com.studai.utils.assembler;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Generic abstract assembler for mapping between Entity and DTO.
 *
 * @param <E> Entity type
 * @param <D> DTO type
 */
public abstract class AbstractAssembler<E, D> {

	/**
	 * Convert an entity to a DTO.
	 */
	public abstract D toDto(E entity);

	/**
	 * Convert a DTO to an entity.
	 */
	public abstract E toEntity(D dto);

	/**
	 * Update an existing entity using a DTO.
	 * Override if needed for patch/update operations.
	 */
	public E updateEntityFromDto(D dto, E entity) {
		throw new UnsupportedOperationException(
				"updateEntityFromDto not implemented in " + this.getClass().getSimpleName()
		);
	}

	/**
	 * Convert a list of entities to a list of DTOs.
	 */
	public List<D> toDtoList(List<E> entities) {
		if (entities == null) return Collections.emptyList();
		return entities.stream()
				.map(this::toDto)
				.collect(Collectors.toList());
	}

	/**
	 * Convert a list of DTOs to a list of entities.
	 */
	public List<E> toEntityList(List<D> dtos) {
		if (dtos == null) return Collections.emptyList();
		return dtos.stream()
				.map(this::toEntity)
				.collect(Collectors.toList());
	}
}
