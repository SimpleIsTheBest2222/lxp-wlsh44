package com.lxp.repository.entity;

import java.time.LocalDateTime;

import com.lxp.repository.entity.enums.EntityStatus;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseEntity {

	private EntityStatus status;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;

	protected void initialize(LocalDateTime now) {
		this.status = EntityStatus.ACTIVE;
		this.createdAt = now;
		this.modifiedAt = now;
	}

	protected void restore(EntityStatus status, LocalDateTime createdAt, LocalDateTime modifiedAt) {
		this.status = status;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
	}

	protected void modified(LocalDateTime now) {
		this.modifiedAt = now;
	}

	protected void delete(LocalDateTime now) {
		this.status = EntityStatus.DELETED;
		this.modifiedAt = now;
	}

	public boolean isDeleted() {
		return status == EntityStatus.DELETED;
	}
}
