package ecom.mta.authdomain;

import ecom.mta.authdomain.model.Email;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EmailTest {
    @Test void shouldRetainValueAndCompareByValue() {
        var first = new Email("user@example.com");
        var second = new Email("user@example.com");
        assertThat(first.value()).isEqualTo("user@example.com");
        assertThat(first).isEqualTo(second).hasSameHashCodeAs(second);
    }

    @Test void shouldRejectNullValue() {
        assertThatThrownBy(() -> new Email(null)).isInstanceOf(RuntimeException.class);
    }

    @Test void shouldNormalizeCaseAndWhitespace() {
        assertThat(new Email("  User@Example.COM ").value()).isEqualTo("user@example.com");
    }

    @Test void shouldRejectInvalidFormat() {
        assertThatThrownBy(() -> new Email("user@localhost"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Email("user example.com"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
