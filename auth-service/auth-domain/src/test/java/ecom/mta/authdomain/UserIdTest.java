package ecom.mta.authdomain;

import ecom.mta.authdomain.model.UserId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserIdTest {

    @Nested
    @DisplayName("Creation & Validation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should successfully create UserId with a valid positive number")
        void shouldCreateValidUserId() {
            UserId userId = UserId.of(42L);

            assertThat(userId.value()).isEqualTo(42L);
        }

        @Test
        @DisplayName("Should successfully create UserId with Long.MAX_VALUE")
        void shouldAcceptMaximumLongValue() {
            UserId userId = UserId.of(Long.MAX_VALUE);

            assertThat(userId.value()).isEqualTo(Long.MAX_VALUE);
        }

        @Test
        @DisplayName("Should throw NullPointerException when value is null")
        void shouldRejectNullValue() {
            assertThatThrownBy(() -> UserId.of(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("User ID cannot be null");
        }

        @ParameterizedTest
        @ValueSource(longs = {0L, -1L, -9999L})
        @DisplayName("Should throw IllegalArgumentException when value is less than or equal to 0")
        void shouldRejectInvalidValues(long invalidValue) {
            assertThatThrownBy(() -> UserId.of(invalidValue))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("User ID must be greater than 0");
        }
    }

    @Nested
    @DisplayName("Equality & HashCode Tests")
    class EqualityTests {

        @Test
        @DisplayName("Should be equal when two instances have the same value")
        void shouldBeEqualForSameValue() {
            UserId first = UserId.of(100L);
            UserId second = UserId.of(100L);

            assertThat(first).isEqualTo(second);
            assertThat(first.hashCode()).isEqualTo(second.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when values are different")
        void shouldNotBeEqualForDifferentValues() {
            UserId first = UserId.of(100L);
            UserId second = UserId.of(200L);

            assertThat(first).isNotEqualTo(second);
            assertThat(first.hashCode()).isNotEqualTo(second.hashCode());
        }

        @Test
        @DisplayName("Should produce different hash codes for distinct boundary values")
        void shouldHaveDifferentHashCodesForDistinctBoundaryValues() {
            UserId minimum = UserId.of(1L);
            UserId maximum = UserId.of(Long.MAX_VALUE);

            assertThat(minimum).isNotEqualTo(maximum);
            assertThat(minimum.hashCode()).isNotEqualTo(maximum.hashCode());
        }

        @Test
        @DisplayName("Should respect basic equals contract properties")
        void shouldRespectEqualsContract() {
            UserId userId = UserId.of(100L);

            assertThat(userId).isNotEqualTo(null);
            assertThat(userId).isEqualTo(userId);
            assertThat(userId).isNotEqualTo("100");
        }
    }
}
