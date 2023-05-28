package com.teamolha.talantino.talent.model.response;

import java.util.*;

public record KindsDTO(
        long amount,
        List<KindDTO> kinds
) {
}
