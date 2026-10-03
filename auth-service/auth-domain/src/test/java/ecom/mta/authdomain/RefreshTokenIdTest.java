package ecom.mta.authdomain;

import ecom.mta.authdomain.model.RefreshTokenId;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RefreshTokenIdTest {
    @Test void shouldExposeValueAndCompareByValue() {
        var first = new RefreshTokenId(42L);
        var second = new RefreshTokenId(42L);
        assertThat(first.value()).isEqualTo(42L);
        assertThat(first).isEqualTo(second).hasSameHashCodeAs(second);
    }

    @Test void shouldRejectNullValue() {
        assertThatThrownBy(() -> new RefreshTokenId(null))
                .isInstanceOf(NullPointerException.class);
    }
}
