package com.elkhami.f1champions.champions.infrastructure.db.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import java.io.Serializable
import java.util.UUID

@Entity
@Table(
    name = "champion_entity",
    uniqueConstraints = [UniqueConstraint(columnNames = ["season"])],
)
data class ChampionEntity(
    @Id
    val id: UUID = UUID.randomUUID(),
    val season: String,
    @Column(name = "driver_id")
    val driverId: String,
    @Column(name = "driver_name")
    val driverName: String,
    val constructor: String,
) : Serializable
