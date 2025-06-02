package com.elkhami.f1champions.seasondetails.intrastructure.db.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import java.io.Serializable
import java.util.UUID

@Entity
@Table(
    name = "season_details_entity",
    uniqueConstraints = [UniqueConstraint(columnNames = ["season", "round"])],
)
data class SeasonDetailsEntity(
    @Id
    val id: UUID = UUID.randomUUID(),
    val season: String,
    val round: String,
    @Column(name = "race_name")
    val raceName: String,
    val date: String,
    @Column(name = "winner_id")
    val winnerId: String,
    @Column(name = "winner_name")
    val winnerName: String,
    val constructor: String,
) : Serializable
