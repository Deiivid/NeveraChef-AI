# DECISIONS

Last updated: 2026-05-15

Format:
- ID
- Date
- Decision
- Context
- Alternatives
- Consequence
- Status

## ADR-001

- Date: 2026-05-15
- Decision: Start Android-first for MVP delivery.
- Context: MVP needs predictable and fast delivery with low integration risk.
- Alternatives:
  1. Full Android+iOS parity from day one.
  2. iOS-first.
- Consequence: Faster MVP, iOS feature parity deferred.
- Status: accepted

## ADR-002

- Date: 2026-05-15
- Decision: Keep local-first and no login in MVP.
- Context: Backend/auth setup would increase scope and risk.
- Alternatives:
  1. Add auth + cloud sync immediately.
  2. Hybrid partial cloud setup.
- Consequence: Less infrastructure complexity, no cross-device sync initially.
- Status: accepted

## ADR-003

- Date: 2026-05-15
- Decision: Use structured JSON-only contract for AI outputs.
- Context: Free-text responses create unstable UI parsing.
- Alternatives:
  1. Free-form text + heuristic extraction.
  2. Semi-structured markdown parsing.
- Consequence: Better reliability and testability, requires schema validation.
- Status: accepted

## ADR-004

- Date: 2026-05-15
- Decision: Use shared Compose Multiplatform UI as default strategy.
- Context: Current repo already wires Android and iOS to shared Compose screen.
- Alternatives:
  1. Split Android Compose and iOS SwiftUI screens now.
  2. Native UI on both platforms from start.
- Consequence: Faster delivery and lower duplication now; native divergence can be introduced later if justified.
- Status: accepted

## ADR-005

- Date: 2026-05-15
- Decision: Modularization must be incremental, not all-at-once.
- Context: Full module explosion early can delay MVP.
- Alternatives:
  1. Immediate full Gradle module split.
  2. Keep single module too long.
- Consequence: Better balance between structure and delivery speed.
- Status: accepted

## ADR-006

- Date: 2026-05-15
- Decision: Voice/camera features are post core recipe-text milestone.
- Context: Multimodal features increase platform complexity.
- Alternatives:
  1. Build multimodal first.
  2. Build all in parallel.
- Consequence: Clear critical path and lower delivery risk.
- Status: accepted

## Pending ADRs

1. ADR-P01 - AI provider choice (OpenAI vs Gemini).
2. ADR-P02 - Persistence staging (DataStore first vs early Room).
3. ADR-P03 - Weekly menu scope classification (MVP vs post-MVP).
