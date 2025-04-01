package com.example.sharedcard.service.dto


import java.util.UUID

data class AccountDeleteResponse(
    val persons: List<UUID>,
    val groups: List<UUID>,
    val groupPersons: List<Pair<UUID,UUID>>,
    val purchases: List<UUID>,
    val baskets: List<UUID>,
    val targets: List<UUID>
)