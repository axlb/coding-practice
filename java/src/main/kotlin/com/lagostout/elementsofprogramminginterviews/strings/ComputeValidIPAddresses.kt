package com.lagostout.elementsofprogramminginterviews.strings

/* Problem 7.10.1  Page 106 */

fun computeValidIPAddresses(addressWithNoPeriods: String): List<String> {

    val maxOctetLength = 3
    val maxOctetOrdinal = 3

    fun computeOctets(addressWithNoPeriods: String,
                      start: Int, octetOrdinal: Int): List<String> {

        // Base case
        if (octetOrdinal > 3 && start == addressWithNoPeriods.count())
            return listOf("")
        else if (octetOrdinal > 3 || start > addressWithNoPeriods.count() ||
                (addressWithNoPeriods.length - start).let {
                        remainingAddressLength ->
                    val remainingOctetCount = maxOctetOrdinal - octetOrdinal + 1
                    remainingAddressLength < remainingOctetCount &&
                            remainingAddressLength > remainingOctetCount * maxOctetLength
                })
            return emptyList()

        val addressWithPeriods = mutableListOf<String>()
        for (currentOctetLength in (1..3)) {
            val nextOctetStart = start + currentOctetLength
            computeOctets(addressWithNoPeriods, nextOctetStart, octetOrdinal + 1).forEach {
                val currentOctet = addressWithNoPeriods.substring(start, nextOctetStart)
                addressWithPeriods.add("$currentOctet${if (it.isNotEmpty()) "." else ""}$it")
            }
        }

        return addressWithPeriods
    }

    return computeOctets(addressWithNoPeriods, 0, 0)
}
