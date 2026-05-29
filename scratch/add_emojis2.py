import json

emojis = {
    "ROOT_ROAD": "🛣",
    "ROAD": "🛣",
    "COTTAGE": "🏠",
    "CONDO": "🏢",
    "SKYSCRAPER": "🏙",
    "FOOD": "🍞",
    "METALLURGICAL": "⚙",
    "PETROCHEMICAL": "🛢",
    "WIND": "🌬",
    "SOLAR": "☀️",
    "INCINERATOR": "🔥",
    "COAL": "🏭",
    "OIL": "🛢",
    "NUCLEAR": "☢",
    "PARK": "🌳",
    "NATURAL_RESERVE": "🌲",
    "NATIONAL_PARK": "🏔",
    "CLINIC": "⚕",
    "HOSPITAL": "🏥",
    "FIRE_STATION": "🚒",
    "CINEMA": "🎬",
    "AMUSEMENT_PARK": "🎢"
}

with open('src/main/resources/buildings.json', 'r', encoding='utf-8') as f:
    data = json.load(f)

for b in data:
    eid = b.get('id')
    if eid in emojis:
        b['emoji'] = emojis[eid]

with open('src/main/resources/buildings.json', 'w', encoding='utf-8') as f:
    json.dump(data, f, indent=4, ensure_ascii=False)

print("Updated buildings.json with emojis")
