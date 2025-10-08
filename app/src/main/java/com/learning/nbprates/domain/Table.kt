package com.learning.nbprates.domain

import com.learning.nbprates.model.Currency
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
// helper type to facilitate deserialization from response
data class Table(val table: String, val no: String, val effectiveDate: String, val rates: List<Currency>)