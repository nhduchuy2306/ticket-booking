const COLLECTION_KEYS = ['items', 'list', 'content', 'data', 'result', 'rows'] as const;
const ID_KEYS = ['id', 'eventId', 'eventID'] as const;

type JsonValue = null | boolean | number | string | JsonValue[];
type JsonObject = Record<string, JsonValue>;

function isObject(value: unknown): value is JsonObject {
  return value !== null && typeof value === 'object' && !Array.isArray(value);
}

export function safeJson(body: string | undefined): JsonValue | null {
  if (!body) {
    return null;
  }

  try {
    return JSON.parse(body) as JsonValue;
  } catch {
    return null;
  }
}

export function unwrapApiResponse(payload: JsonValue | null | undefined): JsonValue | null | undefined {
  if (!isObject(payload)) {
    return payload;
  }

  for (const key of COLLECTION_KEYS) {
    if (Object.prototype.hasOwnProperty.call(payload, key)) {
      return payload[key];
    }
  }

  return payload;
}

export function extractCandidateList(payload: JsonValue | null | undefined): JsonValue[] {
  const root = unwrapApiResponse(payload);

  if (Array.isArray(root)) {
    return root;
  }

  if (isObject(root)) {
    for (const key of COLLECTION_KEYS) {
      const candidate = root[key];
      if (Array.isArray(candidate)) {
        return candidate;
      }
    }
  }

  return [];
}

export function findFirstEntityId(value: JsonValue | null | undefined, visited = new Set<unknown>()): string | null {
  if (!isObject(value)) {
    return null;
  }

  if (visited.has(value)) {
    return null;
  }
  visited.add(value);

  for (const key of ID_KEYS) {
    const candidate = value[key];
    if (typeof candidate === 'string' || typeof candidate === 'number') {
      return String(candidate);
    }
  }

  for (const key of COLLECTION_KEYS) {
    const nested = value[key];
    if (nested === undefined || nested === null) {
      continue;
    }

    const found = findFirstEntityId(nested, visited);
    if (found) {
      return found;
    }
  }

  for (const nested of Object.values(value)) {
    const found = findFirstEntityId(nested, visited);
    if (found) {
      return found;
    }
  }

  return null;
}

export function pickEventId(payload: JsonValue | null | undefined, fallbackEventId = ''): string {
  if (fallbackEventId) {
    return fallbackEventId;
  }

  for (const item of extractCandidateList(payload)) {
    const found = findFirstEntityId(item);
    if (found) {
      return found;
    }
  }

  return findFirstEntityId(unwrapApiResponse(payload)) ?? '';
}

