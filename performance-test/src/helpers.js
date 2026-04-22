const COLLECTION_KEYS = ['items', 'list', 'content', 'data', 'result', 'rows'];
const ID_KEYS = ['id', 'eventId', 'eventID'];

function isObject(value) {
  return value !== null && typeof value === 'object';
}

export function safeJson(response) {
  const body = response?.body;
  if (!body || typeof body !== 'string') {
    return null;
  }

  try {
    return JSON.parse(body);
  } catch {
    return null;
  }
}

export function unwrapApiResponse(payload) {
  if (!isObject(payload) || Array.isArray(payload)) {
    return payload;
  }

  for (const key of COLLECTION_KEYS) {
    if (Object.prototype.hasOwnProperty.call(payload, key)) {
      return payload[key];
    }
  }

  return payload;
}

export function extractCandidateList(payload) {
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

export function findFirstEntityId(value, visited = new Set()) {
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

export function pickEventId(payload, fallbackEventId = '') {
  if (fallbackEventId) {
    return fallbackEventId;
  }

  for (const item of extractCandidateList(payload)) {
    const found = findFirstEntityId(item);
    if (found) {
      return found;
    }
  }

  return findFirstEntityId(unwrapApiResponse(payload));
}

