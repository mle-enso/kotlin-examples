package de.idealo.orca.cataloging

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class ShoeSizesTest {
    // Regular expression to match common shoe size formats including fraction symbols
    val shoeSizeRegex = Regex("""
    \b                           # Word boundary at start
    (\d{1,2}                     # 1-2 digit number
      (?:                        # Non-capturing group for decimal part alternatives
        \.\d{1,2}                # Period followed by 1-2 digits
        |
        \,\d{1,2}                # Comma followed by 1-2 digits
        |
        \s*½                     # Optional whitespace followed by fraction symbol
      )?                         # The decimal part is optional
    )                            # End of capture group
    """.trimIndent(), RegexOption.COMMENTS)

    // do not use word boundary at end \b
    // if at all: (?=\s|$|[,.;:!?)])           # Look ahead for whitespace, end of string, or punctuation

    private fun textsAndSizes(): List<Arguments> {
        return listOf(
            Arguments.of("I wear size 8.5 and that's it.", "8.5"),
            Arguments.of("The 9 shoes", "9"),
            Arguments.of("EU 42", "42"),
            Arguments.of("EU 43,5", "43,5"),
            Arguments.of("Size UK 7.5 fits me well", "7.5"),
            Arguments.of("I need 10.5 US shoes", "10.5"),
            Arguments.of("The shoes come in EU 39", "39"),
            Arguments.of("I wear size 39 ½", "39 ½"),     // Example with fraction symbol
            Arguments.of("I wear size 39   ½ and that's it.", "39   ½"),     // Example with fraction symbol
            Arguments.of("EU 40½ is my size", "40½"),      // Example with fraction symbol (no space)
        )
    }

    @ParameterizedTest
    @MethodSource("textsAndSizes")
    fun findShoeSize(text: String, result: String) {
        val matches = shoeSizeRegex.findAll(text)
        matches.forEach { match ->
            val matchedText = match.value
            val sizeValue = match.groupValues[1]

            println("====== ${match.groupValues}")

            // For display purposes, convert ½ to .5 in the extracted size
            val normalizedSize = if (sizeValue.contains("½")) {
                sizeValue.replace("\\s*½".toRegex(), ".5")
            } else {
                sizeValue
            }

            println("Found shoe size in '$text': $matchedText (normalized size: $normalizedSize)")
        }
        assertThat(matches.iterator().next().groupValues[0]).isEqualTo(result)
    }
}
