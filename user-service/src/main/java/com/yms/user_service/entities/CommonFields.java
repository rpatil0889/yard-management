package com.yms.user_service.entities;


import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
@Data
public class CommonFields implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The created by. */
    @Column(name = "created_by")
    protected Long createdBy;

    /** The created on. */
    @Column(name = "created_on")
    protected LocalDateTime createdOn;

    /** The updated by. */
    @Column(name = "updated_by")
    protected Long updatedBy;

    /** The updated on. */
    @Column(name = "updated_on")
    protected LocalDateTime updatedOn;
}
