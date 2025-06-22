import { VenueMap } from "../models/generated/event-service-models";

const createMockVenueData = (): VenueMap => ({
    "id": "stadium-001",
    "name": "National Stadium Seat Map",
    "dimensions": {
        "width": 1200.0,
        "height": 800.0
    },
    "stageConfig": {
        "id": "stage-001",
        "name": "Main Stage",
        "description": "Primary performance stage",
        "position": {
            "x": 50.0,
            "y": 20.0
        },
        "dimensions": {
            "width": 1100.0,
            "height": 150.0
        },
        "rotation": 0.0,
        "shape": "RECTANGLE",
        "orientation": "SOUTH",
        "active": true,
        "customVertices": [],
        "elevation": 2.0,
        "svgPath": undefined
    },
    "seatConfig": {
        "seatTypeColors": {
            "VIP": "#FFD700",
            "Premium": "#FF6B6B",
            "Standard": "#4ECDC4",
            "Economy": "#95E1D3",
            "Selected": "#FF6347",
        },
        "sections": [
            {
                "id": "section-vip-001",
                "name": "VIP Section A",
                "type": "SEATED",
                "position": {
                    "x": 400.0,
                    "y": 200.0
                },
                "dimensions": {
                    "width": 400.0,
                    "height": 200.0
                },
                "rotation": 0.0,
                "capacity": 120,
                "isArc": false,
                "arcProperties": undefined,
                "ticketTypeId": "VIP",
                "rows": [
                    {
                        "id": "row-vip-001-a",
                        "name": "Row A",
                        "position": {
                            "x": 5.0,
                            "y": 40.0
                        },
                        "isArc": false,
                        "arcProperties": undefined,
                        "seats": [
                            {
                                "id": "seat-vip-001-a-1",
                                "name": "1",
                                "position": {
                                    "x": 0.0,
                                    "y": 0.0
                                },
                                "status": "AVAILABLE",
                                "attributes": {
                                    "qualityScore": true,
                                    "premiumSeat": true,
                                    "hasArmrest": true,
                                    "extraLegroom": true
                                },
                                "useAbsolutePosition": false,
                                "absolutePosition": undefined,
                                "ticketTypeId": "VIP",
                                "visualStyle": {
                                    "fillColor": "#FFD700",
                                    "strokeColor": "#000000",
                                    "strokeWidth": 2.0,
                                    "opacity": 1.0,
                                    "dashPattern": undefined,
                                    "visible": true,
                                    "zindex": 3
                                },
                                "interactiveProperties": {
                                    "draggable": false,
                                    "selectable": true,
                                    "hoverAble": true,
                                    "cursor": "pointer",
                                    "eventHandlers": {}
                                }
                            },
                            {
                                "id": "seat-vip-001-a-2",
                                "name": "2",
                                "position": {
                                    "x": 50.0,
                                    "y": 0.0
                                },
                                "status": "RESERVED",
                                "attributes": {
                                    "qualityScore": true,
                                    "premiumSeat": true,
                                    "hasArmrest": true,
                                    "extraLegroom": true
                                },
                                "useAbsolutePosition": false,
                                "absolutePosition": undefined,
                                "ticketTypeId": "VIP",
                                "visualStyle": {
                                    "fillColor": "#FFFF00",
                                    "strokeColor": "#000000",
                                    "strokeWidth": 2.0,
                                    "opacity": 1.0,
                                    "dashPattern": undefined,
                                    "visible": true,
                                    "zindex": 3
                                },
                                "interactiveProperties": {
                                    "draggable": false,
                                    "selectable": false,
                                    "hoverAble": true,
                                    "cursor": "not-allowed",
                                    "eventHandlers": {}
                                }
                            },
                            {
                                "id": "seat-vip-001-a-3",
                                "name": "3",
                                "position": {
                                    "x": 100.0,
                                    "y": 0.0
                                },
                                "status": "SOLD",
                                "attributes": {
                                    "qualityScore": true,
                                    "premiumSeat": true,
                                    "hasArmrest": true,
                                    "extraLegroom": true
                                },
                                "useAbsolutePosition": false,
                                "absolutePosition": undefined,
                                "ticketTypeId": "VIP",
                                "visualStyle": {
                                    "fillColor": "#FF0000",
                                    "strokeColor": "#000000",
                                    "strokeWidth": 2.0,
                                    "opacity": 1.0,
                                    "dashPattern": undefined,
                                    "visible": true,
                                    "zindex": 3
                                },
                                "interactiveProperties": {
                                    "draggable": false,
                                    "selectable": false,
                                    "hoverAble": false,
                                    "cursor": "default",
                                    "eventHandlers": {}
                                }
                            },
                            {
                                "id": "seat-vip-001-a-4",
                                "name": "4",
                                "position": {
                                    "x": 150.0,
                                    "y": 0.0
                                },
                                "status": "AVAILABLE",
                                "attributes": {
                                    "qualityScore": true,
                                    "premiumSeat": true,
                                    "hasArmrest": true,
                                    "extraLegroom": true
                                },
                                "useAbsolutePosition": false,
                                "absolutePosition": undefined,
                                "ticketTypeId": "VIP",
                                "visualStyle": {
                                    "fillColor": "#FFD700",
                                    "strokeColor": "#000000",
                                    "strokeWidth": 2.0,
                                    "opacity": 1.0,
                                    "dashPattern": undefined,
                                    "visible": true,
                                    "zindex": 3
                                },
                                "interactiveProperties": {
                                    "draggable": false,
                                    "selectable": true,
                                    "hoverAble": true,
                                    "cursor": "pointer",
                                    "eventHandlers": {}
                                }
                            },
                            {
                                "id": "seat-vip-001-a-5",
                                "name": "5",
                                "position": {
                                    "x": 200.0,
                                    "y": 0.0
                                },
                                "status": "AVAILABLE",
                                "attributes": {
                                    "qualityScore": true,
                                    "premiumSeat": true,
                                    "hasArmrest": true,
                                    "extraLegroom": true
                                },
                                "useAbsolutePosition": false,
                                "absolutePosition": undefined,
                                "ticketTypeId": "VIP",
                                "visualStyle": {
                                    "fillColor": "#0000FF",
                                    "strokeColor": "#000000",
                                    "strokeWidth": 2.0,
                                    "opacity": 0.7,
                                    "dashPattern": ["5", "5"],
                                    "visible": true,
                                    "zindex": 3
                                },
                                "interactiveProperties": {
                                    "draggable": false,
                                    "selectable": false,
                                    "hoverAble": false,
                                    "cursor": "not-allowed",
                                    "eventHandlers": {}
                                }
                            },
                            {
                                "id": "seat-vip-001-a-6",
                                "name": "6",
                                "position": {
                                    "x": 250.0,
                                    "y": 0.0
                                },
                                "status": "AVAILABLE",
                                "attributes": {
                                    "qualityScore": true,
                                    "premiumSeat": true,
                                    "hasArmrest": true,
                                    "extraLegroom": true
                                },
                                "useAbsolutePosition": false,
                                "absolutePosition": undefined,
                                "ticketTypeId": "VIP",
                                "visualStyle": {
                                    "fillColor": "#FFD700",
                                    "strokeColor": "#000000",
                                    "strokeWidth": 2.0,
                                    "opacity": 1.0,
                                    "dashPattern": undefined,
                                    "visible": true,
                                    "zindex": 3
                                },
                                "interactiveProperties": {
                                    "draggable": false,
                                    "selectable": true,
                                    "hoverAble": true,
                                    "cursor": "pointer",
                                    "eventHandlers": {}
                                }
                            },
                            {
                                "id": "seat-vip-001-a-7",
                                "name": "7",
                                "position": {
                                    "x": 300.0,
                                    "y": 0.0
                                },
                                "status": "AVAILABLE",
                                "attributes": {
                                    "qualityScore": true,
                                    "premiumSeat": true,
                                    "hasArmrest": true,
                                    "extraLegroom": true
                                },
                                "useAbsolutePosition": false,
                                "absolutePosition": undefined,
                                "ticketTypeId": "VIP",
                                "visualStyle": {
                                    "fillColor": "#FFD700",
                                    "strokeColor": "#000000",
                                    "strokeWidth": 2.0,
                                    "opacity": 1.0,
                                    "dashPattern": undefined,
                                    "visible": true,
                                    "zindex": 3
                                },
                                "interactiveProperties": {
                                    "draggable": false,
                                    "selectable": true,
                                    "hoverAble": true,
                                    "cursor": "pointer",
                                    "eventHandlers": {}
                                }
                            },
                            {
                                "id": "seat-vip-001-a-8",
                                "name": "8",
                                "position": {
                                    "x": 350.0,
                                    "y": 0.0
                                },
                                "status": "AVAILABLE",
                                "attributes": {
                                    "qualityScore": true,
                                    "premiumSeat": true,
                                    "hasArmrest": true,
                                    "extraLegroom": true
                                },
                                "useAbsolutePosition": false,
                                "absolutePosition": undefined,
                                "ticketTypeId": "VIP",
                                "visualStyle": {
                                    "fillColor": "#FFD700",
                                    "strokeColor": "#000000",
                                    "strokeWidth": 2.0,
                                    "opacity": 1.0,
                                    "dashPattern": undefined,
                                    "visible": true,
                                    "zindex": 3
                                },
                                "interactiveProperties": {
                                    "draggable": false,
                                    "selectable": true,
                                    "hoverAble": true,
                                    "cursor": "pointer",
                                    "eventHandlers": {}
                                }
                            }
                        ]
                    },
                    {
                        "id": "row-vip-001-b",
                        "name": "Row B",
                        "position": {
                            "x": 5.0,
                            "y": 90.0
                        },
                        "isArc": false,
                        "arcProperties": undefined,
                        "seats": [
                            {
                                "id": "seat-vip-001-b-1",
                                "name": "1",
                                "position": {
                                    "x": 0.0,
                                    "y": 0.0
                                },
                                "status": "SOLD",
                                "attributes": {
                                    "qualityScore": true,
                                    "premiumSeat": true,
                                    "hasArmrest": true,
                                    "extraLegroom": true
                                },
                                "useAbsolutePosition": false,
                                "absolutePosition": undefined,
                                "ticketTypeId": "VIP",
                                "visualStyle": {
                                    "fillColor": "#FF0000",
                                    "strokeColor": "#000000",
                                    "strokeWidth": 2.0,
                                    "opacity": 1.0,
                                    "dashPattern": undefined,
                                    "visible": true,
                                    "zindex": 3
                                },
                                "interactiveProperties": {
                                    "draggable": false,
                                    "selectable": false,
                                    "hoverAble": false,
                                    "cursor": "default",
                                    "eventHandlers": {}
                                }
                            },
                            {
                                "id": "seat-vip-001-b-2",
                                "name": "2",
                                "position": {
                                    "x": 50.0,
                                    "y": 0.0
                                },
                                "status": "AVAILABLE",
                                "attributes": {
                                    "qualityScore": true,
                                    "premiumSeat": true,
                                    "hasArmrest": true,
                                    "extraLegroom": true
                                },
                                "useAbsolutePosition": false,
                                "absolutePosition": undefined,
                                "ticketTypeId": "VIP",
                                "visualStyle": {
                                    "fillColor": "#FFD700",
                                    "strokeColor": "#000000",
                                    "strokeWidth": 2.0,
                                    "opacity": 1.0,
                                    "dashPattern": undefined,
                                    "visible": true,
                                    "zindex": 3
                                },
                                "interactiveProperties": {
                                    "draggable": false,
                                    "selectable": true,
                                    "hoverAble": true,
                                    "cursor": "pointer",
                                    "eventHandlers": {}
                                }
                            },
                            {
                                "id": "seat-vip-001-b-3",
                                "name": "3",
                                "position": {
                                    "x": 100.0,
                                    "y": 0.0
                                },
                                "status": "AVAILABLE",
                                "attributes": {
                                    "qualityScore": true,
                                    "premiumSeat": true,
                                    "hasArmrest": true,
                                    "extraLegroom": true
                                },
                                "useAbsolutePosition": false,
                                "absolutePosition": undefined,
                                "ticketTypeId": "VIP",
                                "visualStyle": {
                                    "fillColor": "#FFD700",
                                    "strokeColor": "#000000",
                                    "strokeWidth": 2.0,
                                    "opacity": 1.0,
                                    "dashPattern": undefined,
                                    "visible": true,
                                    "zindex": 3
                                },
                                "interactiveProperties": {
                                    "draggable": false,
                                    "selectable": true,
                                    "hoverAble": true,
                                    "cursor": "pointer",
                                    "eventHandlers": {}
                                }
                            },
                            {
                                "id": "seat-vip-001-b-4",
                                "name": "4",
                                "position": {
                                    "x": 150.0,
                                    "y": 0.0
                                },
                                "status": "AVAILABLE",
                                "attributes": {
                                    "qualityScore": true,
                                    "premiumSeat": true,
                                    "hasArmrest": true,
                                    "extraLegroom": true
                                },
                                "useAbsolutePosition": false,
                                "absolutePosition": undefined,
                                "ticketTypeId": "VIP",
                                "visualStyle": {
                                    "fillColor": "#FFD700",
                                    "strokeColor": "#000000",
                                    "strokeWidth": 2.0,
                                    "opacity": 1.0,
                                    "dashPattern": undefined,
                                    "visible": true,
                                    "zindex": 3
                                },
                                "interactiveProperties": {
                                    "draggable": false,
                                    "selectable": true,
                                    "hoverAble": true,
                                    "cursor": "pointer",
                                    "eventHandlers": {}
                                }
                            },
                            {
                                "id": "seat-vip-001-b-5",
                                "name": "5",
                                "position": {
                                    "x": 200.0,
                                    "y": 0.0
                                },
                                "status": "AVAILABLE",
                                "attributes": {
                                    "qualityScore": true,
                                    "premiumSeat": true,
                                    "hasArmrest": true,
                                    "extraLegroom": true
                                },
                                "useAbsolutePosition": false,
                                "absolutePosition": undefined,
                                "ticketTypeId": "VIP",
                                "visualStyle": {
                                    "fillColor": "#FFD700",
                                    "strokeColor": "#000000",
                                    "strokeWidth": 2.0,
                                    "opacity": 1.0,
                                    "dashPattern": undefined,
                                    "visible": true,
                                    "zindex": 3
                                },
                                "interactiveProperties": {
                                    "draggable": false,
                                    "selectable": true,
                                    "hoverAble": true,
                                    "cursor": "pointer",
                                    'eventHandlers': {}
                                }
                            },
                            {
                                "id": "seat-vip-001-b-6",
                                "name": "6",
                                "position": {
                                    "x": 250.0,
                                    "y": 0.0
                                },
                                "status": "AVAILABLE",
                                "attributes": {
                                    "qualityScore": true,
                                    "premiumSeat": true,
                                    "hasArmrest": true,
                                    "extraLegroom": true
                                },
                                "useAbsolutePosition": false,
                                "absolutePosition": undefined,
                                "ticketTypeId": "VIP",
                                "visualStyle": {
                                    "fillColor": "#FFD700",
                                    "strokeColor": "#000000",
                                    "strokeWidth": 2.0,
                                    "opacity": 1.0,
                                    "dashPattern": undefined,
                                    "visible": true,
                                    "zindex": 3
                                },
                                "interactiveProperties": {
                                    "draggable": false,
                                    "selectable": true,
                                    "hoverAble": true,
                                    "cursor": "pointer",
                                    'eventHandlers': {}
                                }
                            },
                            {
                                "id": "seat-vip-001-b-7",
                                "name": "7",
                                "position": {
                                    "x": 300.0,
                                    "y": 0.0
                                },
                                "status": "AVAILABLE",
                                "attributes": {
                                    "qualityScore": true,
                                    "premiumSeat": true,
                                    "hasArmrest": true,
                                    "extraLegroom": true
                                },
                                "useAbsolutePosition": false,
                                "absolutePosition": undefined,
                                "ticketTypeId": "VIP",
                                "visualStyle": {
                                    "fillColor": "#FFD700",
                                    "strokeColor": "#000000",
                                    "strokeWidth": 2.0,
                                    "opacity": 1.0,
                                    "dashPattern": undefined,
                                    'visible': true,
                                    'zindex': 3
                                },
                                'interactiveProperties': {
                                    'draggable': false,
                                    'selectable': true,
                                    'hoverAble': true,
                                    'cursor': 'pointer',
                                    'eventHandlers': {}
                                }
                            },
                            {
                                'id': 'seat-vip-001-b-8',
                                'name': '8',
                                'position': {
                                    'x': 350.0,
                                    'y': 0.0
                                },
                                'status': 'AVAILABLE',
                                'attributes': {
                                    'qualityScore': true,
                                    'premiumSeat': true,
                                    'hasArmrest': true,
                                    'extraLegroom': true
                                },
                                'useAbsolutePosition': false,
                                'absolutePosition': undefined,
                                'ticketTypeId': 'VIP',
                                'visualStyle': {
                                    'fillColor': '#FFD700',
                                    'strokeColor': '#000000',
                                    'strokeWidth': 2.0,
                                    'opacity': 1.0,
                                    'dashPattern': undefined,
                                    'visible': true,
                                    'zindex': 3
                                },
                                'interactiveProperties': {
                                    'draggable': false,
                                    'selectable': true,
                                    'hoverAble': true,
                                    'cursor': 'pointer',
                                    'eventHandlers': {}
                                }
                            }
                        ]
                    },
                    {
                        "id": "row-vip-001-c",
                        "name": "Row C",
                        "position": {
                            "x": 5.0,
                            "y": 140.0
                        },
                        "isArc": false,
                        "arcProperties": undefined,
                        "seats": [
                            {
                                "id": "seat-vip-001-c-1",
                                "name": "1",
                                "position": {
                                    "x": 0.0,
                                    "y": 0.0
                                },
                                "status": "SOLD",
                                "attributes": {
                                    "qualityScore": true,
                                    "premiumSeat": true,
                                    "hasArmrest": true,
                                    "extraLegroom": true
                                },
                                "useAbsolutePosition": false,
                                "absolutePosition": undefined,
                                "ticketTypeId": "VIP",
                                "visualStyle": {
                                    "fillColor": "#FF0000",
                                    "strokeColor": "#000000",
                                    "strokeWidth": 2.0,
                                    "opacity": 1.0,
                                    "dashPattern": undefined,
                                    "visible": true,
                                    "zindex": 3
                                },
                                "interactiveProperties": {
                                    "draggable": false,
                                    "selectable": false,
                                    "hoverAble": false,
                                    "cursor": "default",
                                    "eventHandlers": {}
                                }
                            },
                            {
                                "id": "seat-vip-001-c-2",
                                "name": "2",
                                "position": {
                                    "x": 50.0,
                                    "y": 0.0
                                },
                                "status": "AVAILABLE",
                                "attributes": {
                                    "qualityScore": true,
                                    "premiumSeat": true,
                                    "hasArmrest": true,
                                    "extraLegroom": true
                                },
                                "useAbsolutePosition": false,
                                "absolutePosition": undefined,
                                "ticketTypeId": "VIP",
                                "visualStyle": {
                                    "fillColor": "#FFD700",
                                    "strokeColor": "#000000",
                                    "strokeWidth": 2.0,
                                    "opacity": 1.0,
                                    "dashPattern": undefined,
                                    "visible": true,
                                    "zindex": 3
                                },
                                "interactiveProperties": {
                                    "draggable": false,
                                    "selectable": true,
                                    "hoverAble": true,
                                    "cursor": "pointer",
                                    "eventHandlers": {}
                                }
                            },
                            {
                                "id": "seat-vip-001-c-3",
                                "name": "3",
                                "position": {
                                    "x": 100.0,
                                    "y": 0.0
                                },
                                "status": "AVAILABLE",
                                "attributes": {
                                    "qualityScore": true,
                                    "premiumSeat": true,
                                    "hasArmrest": true,
                                    "extraLegroom": true
                                },
                                "useAbsolutePosition": false,
                                "absolutePosition": undefined,
                                "ticketTypeId": "VIP",
                                "visualStyle": {
                                    "fillColor": "#FFD700",
                                    "strokeColor": "#000000",
                                    "strokeWidth": 2.0,
                                    "opacity": 1.0,
                                    "dashPattern": undefined,
                                    "visible": true,
                                    "zindex": 3
                                },
                                "interactiveProperties": {
                                    "draggable": false,
                                    "selectable": true,
                                    "hoverAble": true,
                                    "cursor": "pointer",
                                    "eventHandlers": {}
                                }
                            },
                            {
                                "id": "seat-vip-001-c-4",
                                "name": "4",
                                "position": {
                                    "x": 150.0,
                                    "y": 0.0
                                },
                                "status": "AVAILABLE",
                                "attributes": {
                                    "qualityScore": true,
                                    "premiumSeat": true,
                                    "hasArmrest": true,
                                    "extraLegroom": true
                                },
                                "useAbsolutePosition": false,
                                "absolutePosition": undefined,
                                "ticketTypeId": "VIP",
                                "visualStyle": {
                                    "fillColor": "#FFD700",
                                    "strokeColor": "#000000",
                                    "strokeWidth": 2.0,
                                    "opacity": 1.0,
                                    "dashPattern": undefined,
                                    "visible": true,
                                    "zindex": 3
                                },
                                "interactiveProperties": {
                                    "draggable": false,
                                    "selectable": true,
                                    "hoverAble": true,
                                    "cursor": "pointer",
                                    "eventHandlers": {}
                                }
                            },
                            {
                                "id": "seat-vip-001-c-5",
                                "name": "5",
                                "position": {
                                    "x": 200.0,
                                    "y": 0.0
                                },
                                "status": "AVAILABLE",
                                "attributes": {
                                    "qualityScore": true,
                                    "premiumSeat": true,
                                    "hasArmrest": true,
                                    "extraLegroom": true
                                },
                                "useAbsolutePosition": false,
                                "absolutePosition": undefined,
                                "ticketTypeId": "VIP",
                                "visualStyle": {
                                    "fillColor": "#FFD700",
                                    "strokeColor": "#000000",
                                    "strokeWidth": 2.0,
                                    "opacity": 1.0,
                                    "dashPattern": undefined,
                                    "visible": true,
                                    "zindex": 3
                                },
                                "interactiveProperties": {
                                    "draggable": false,
                                    "selectable": true,
                                    "hoverAble": true,
                                    "cursor": "pointer",
                                    'eventHandlers': {}
                                }
                            },
                            {
                                "id": "seat-vip-001-c-6",
                                "name": "6",
                                "position": {
                                    "x": 250.0,
                                    "y": 0.0
                                },
                                "status": "AVAILABLE",
                                "attributes": {
                                    "qualityScore": true,
                                    "premiumSeat": true,
                                    "hasArmrest": true,
                                    "extraLegroom": true
                                },
                                "useAbsolutePosition": false,
                                "absolutePosition": undefined,
                                "ticketTypeId": "VIP",
                                "visualStyle": {
                                    "fillColor": "#FFD700",
                                    "strokeColor": "#000000",
                                    "strokeWidth": 2.0,
                                    "opacity": 1.0,
                                    "dashPattern": undefined,
                                    "visible": true,
                                    "zindex": 3
                                },
                                "interactiveProperties": {
                                    "draggable": false,
                                    "selectable": true,
                                    "hoverAble": true,
                                    "cursor": "pointer",
                                    'eventHandlers': {}
                                }
                            },
                            {
                                "id": "seat-vip-001-c-7",
                                "name": "7",
                                "position": {
                                    "x": 300.0,
                                    "y": 0.0
                                },
                                "status": "AVAILABLE",
                                "attributes": {
                                    "qualityScore": true,
                                    "premiumSeat": true,
                                    "hasArmrest": true,
                                    "extraLegroom": true
                                },
                                "useAbsolutePosition": false,
                                "absolutePosition": undefined,
                                "ticketTypeId": "VIP",
                                "visualStyle": {
                                    "fillColor": "#FFD700",
                                    "strokeColor": "#000000",
                                    "strokeWidth": 2.0,
                                    "opacity": 1.0,
                                    "dashPattern": undefined,
                                    'visible': true,
                                    'zindex': 3
                                },
                                'interactiveProperties': {
                                    'draggable': false,
                                    'selectable': true,
                                    'hoverAble': true,
                                    'cursor': 'pointer',
                                    'eventHandlers': {}
                                }
                            },
                            {
                                'id': 'seat-vip-001-c-8',
                                'name': '8',
                                'position': {
                                    'x': 350.0,
                                    'y': 0.0
                                },
                                'status': 'AVAILABLE',
                                'attributes': {
                                    'qualityScore': true,
                                    'premiumSeat': true,
                                    'hasArmrest': true,
                                    'extraLegroom': true
                                },
                                'useAbsolutePosition': false,
                                'absolutePosition': undefined,
                                'ticketTypeId': 'VIP',
                                'visualStyle': {
                                    'fillColor': '#FFD700',
                                    'strokeColor': '#000000',
                                    'strokeWidth': 2.0,
                                    'opacity': 1.0,
                                    'dashPattern': undefined,
                                    'visible': true,
                                    'zindex': 3
                                },
                                'interactiveProperties': {
                                    'draggable': false,
                                    'selectable': true,
                                    'hoverAble': true,
                                    'cursor': 'pointer',
                                    'eventHandlers': {}
                                }
                            }
                        ]
                    },
                ],
                "tables": []
            },
            {
                "id": "section-premium-001",
                "name": "Premium Section B",
                "type": "SEATED",
                "position": {
                    "x": 50.0,
                    "y": 450.0
                },
                "dimensions": {
                    "width": 750.0,
                    "height": 200.0
                },
                "rotation": 0.0,
                "capacity": 240,
                "isArc": true,
                "arcProperties": {
                    "centerX": 400.0,
                    "centerY": 0.0,
                    "radius": 350.0,
                    "startAngle": 30.0,
                    "endAngle": 150.0
                },
                "ticketTypeId": "Premium",
                "rows": [
                    {
                        "id": "row-premium-001-c",
                        "name": "Row C",
                        "position": {
                            "x": 0.0,
                            "y": 50.0
                        },
                        "isArc": true,
                        "arcProperties": {
                            "centerX": 400.0,
                            "centerY": 0.0,
                            "radius": 300.0,
                            "startAngle": 45.0,
                            "endAngle": 135.0
                        },
                        "seats": [
                            {
                                "id": "seat-premium-001-c-1",
                                "name": "1",
                                "position": {
                                    "x": 283.0,
                                    "y": 212.0
                                },
                                "status": "AVAILABLE",
                                "attributes": {
                                    "qualityScore": true,
                                    "premiumSeat": false,
                                    "hasArmrest": true,
                                    "extraLegroom": false
                                },
                                "useAbsolutePosition": false,
                                "absolutePosition": undefined,
                                "ticketTypeId": "Premium",
                                "visualStyle": {
                                    "fillColor": "#FF6B6B",
                                    "strokeColor": "#000000",
                                    "strokeWidth": 1.5,
                                    "opacity": 1.0,
                                    "dashPattern": undefined,
                                    "visible": true,
                                    "zindex": 3
                                },
                                "interactiveProperties": {
                                    "draggable": false,
                                    "selectable": true,
                                    "hoverAble": true,
                                    "cursor": "pointer",
                                    "eventHandlers": {}
                                }
                            }
                        ]
                    }
                ],
                "tables": []
            },
            {
                "id": "section-table-001",
                "name": "VIP Dining Area",
                "type": "TABLE",
                "position": {
                    "x": 50.0,
                    "y": 200.0
                },
                "dimensions": {
                    "width": 300.0,
                    "height": 200.0
                },
                "rotation": 0.0,
                "capacity": 32,
                "isArc": false,
                "arcProperties": undefined,
                "ticketTypeId": "VIP",
                "rows": [],
                "tables": [
                    {
                        "id": "table-001",
                        "name": "Table 1",
                        "position": {
                            "x": 40.0,
                            "y": 60.0
                        },
                        "dimensions": {
                            "width": 80.0,
                            "height": 80.0
                        },
                        "shape": "ROUND",
                        "capacity": 8,
                        "seats": [
                            {
                                "id": "seat-table-001-1",
                                "name": "1",
                                "position": {
                                    "x": 40.0,
                                    "y": 15.0
                                },
                                "status": "AVAILABLE",
                                "attributes": {
                                    "qualityScore": true,
                                    "premiumSeat": true,
                                    "diningTable": true,
                                    "waitService": true
                                },
                                "useAbsolutePosition": false,
                                "absolutePosition": undefined,
                                "ticketTypeId": "VIP",
                                "visualStyle": {
                                    "fillColor": "#FFD700",
                                    "strokeColor": "#8B4513",
                                    "strokeWidth": 2.0,
                                    "opacity": 1.0,
                                    "dashPattern": undefined,
                                    "visible": true,
                                    "zindex": 3
                                },
                                "interactiveProperties": {
                                    "draggable": false,
                                    "selectable": true,
                                    "hoverAble": true,
                                    "cursor": "pointer",
                                    "eventHandlers": {}
                                }
                            },
                            {
                                "id": "seat-table-001-2",
                                "name": "2",
                                "position": {
                                    "x": 68.3,
                                    "y": 25.0
                                },
                                "status": "AVAILABLE",
                                "attributes": {
                                    "qualityScore": true,
                                    "premiumSeat": true,
                                    "diningTable": true,
                                    "waitService": true
                                },
                                "useAbsolutePosition": false,
                                "absolutePosition": undefined,
                                "ticketTypeId": "VIP",
                                "visualStyle": {
                                    "fillColor": "#FFD700",
                                    "strokeColor": "#8B4513",
                                    "strokeWidth": 2.0,
                                    "opacity": 1.0,
                                    "dashPattern": undefined,
                                    "visible": true,
                                    "zindex": 3
                                },
                                "interactiveProperties": {
                                    "draggable": false,
                                    "selectable": true,
                                    "hoverAble": true,
                                    "cursor": "pointer",
                                    "eventHandlers": {}
                                }
                            },
                            {
                                "id": "seat-table-001-3",
                                "name": "3",
                                "position": {
                                    "x": 75.0,
                                    "y": 40.0
                                },
                                "status": "AVAILABLE",
                                "attributes": {
                                    "qualityScore": true,
                                    "premiumSeat": true,
                                    "diningTable": true,
                                    "waitService": true
                                },
                                "useAbsolutePosition": false,
                                "absolutePosition": undefined,
                                "ticketTypeId": "VIP",
                                "visualStyle": {
                                    "fillColor": "#FFD700",
                                    "strokeColor": "#8B4513",
                                    "strokeWidth": 2.0,
                                    "opacity": 1.0,
                                    "dashPattern": undefined,
                                    "visible": true,
                                    "zindex": 3
                                },
                                "interactiveProperties": {
                                    "draggable": false,
                                    "selectable": true,
                                    "hoverAble": true,
                                    "cursor": "pointer",
                                    "eventHandlers": {}
                                }
                            },
                            {
                                "id": "seat-table-001-4",
                                "name": "4",
                                "position": {
                                    "x": 68.3,
                                    "y": 55.0
                                },
                                "status": "BLOCKED",
                                "attributes": {
                                    "qualityScore": true,
                                    "premiumSeat": true,
                                    "diningTable": true,
                                    "waitService": true
                                },
                                "useAbsolutePosition": false,
                                "absolutePosition": undefined,
                                "ticketTypeId": "VIP",
                                "visualStyle": {
                                    "fillColor": "#0000FF",
                                    "strokeColor": "#8B4513",
                                    "strokeWidth": 2.0,
                                    "opacity": 0.7,
                                    "dashPattern": ["5", "5"],
                                    "visible": true,
                                    "zindex": 3
                                },
                                "interactiveProperties": {
                                    "draggable": false,
                                    "selectable": false,
                                    "hoverAble": false,
                                    "cursor": "not-allowed",
                                    "eventHandlers": {}
                                }
                            },
                            {
                                "id": "seat-table-001-5",
                                "name": "5",
                                "position": {
                                    "x": 40.0,
                                    "y": 65.0
                                },
                                "status": "AVAILABLE",
                                "attributes": {
                                    "qualityScore": true,
                                    "premiumSeat": true,
                                    "diningTable": true,
                                    "waitService": true
                                },
                                "useAbsolutePosition": false,
                                "absolutePosition": undefined,
                                "ticketTypeId": "VIP",
                                "visualStyle": {
                                    "fillColor": "#FFD700",
                                    "strokeColor": "#8B4513",
                                    "strokeWidth": 2.0,
                                    "opacity": 1.0,
                                    "dashPattern": undefined,
                                    "visible": true,
                                    "zindex": 3
                                },
                                "interactiveProperties": {
                                    "draggable": false,
                                    "selectable": true,
                                    "hoverAble": true,
                                    "cursor": "pointer",
                                    "eventHandlers": {}
                                }
                            },
                            {
                                "id": "seat-table-001-6",
                                "name": "6",
                                "position": {
                                    "x": 11.7,
                                    "y": 55.0
                                },
                                "status": "AVAILABLE",
                                "attributes": {
                                    "qualityScore": true,
                                    "premiumSeat": true,
                                    "diningTable": true,
                                    "waitService": true
                                },
                                "useAbsolutePosition": false,
                                "absolutePosition": undefined,
                                "ticketTypeId": "VIP",
                                "visualStyle": {
                                    "fillColor": "#FFD700",
                                    "strokeColor": "#8B4513",
                                    "strokeWidth": 2.0,
                                    "opacity": 1.0,
                                    "dashPattern": undefined,
                                    "visible": true,
                                    "zindex": 3
                                },
                                "interactiveProperties": {
                                    "draggable": false,
                                    "selectable": true,
                                    "hoverAble": true,
                                    "cursor": "pointer",
                                    "eventHandlers": {}
                                }
                            },
                            {
                                "id": "seat-table-001-7",
                                "name": "7",
                                "position": {
                                    "x": 5.0,
                                    "y": 40.0
                                },
                                "status": "AVAILABLE",
                                "attributes": {
                                    "qualityScore": true,
                                    "premiumSeat": true,
                                    "diningTable": true,
                                    "waitService": true
                                },
                                "useAbsolutePosition": false,
                                "absolutePosition": undefined,
                                "ticketTypeId": "VIP",
                                "visualStyle": {
                                    "fillColor": "#FFD700",
                                    "strokeColor": "#8B4513",
                                    "strokeWidth": 2.0,
                                    "opacity": 1.0,
                                    "dashPattern": undefined,
                                    "visible": true,
                                    "zindex": 3
                                },
                                "interactiveProperties": {
                                    "draggable": false,
                                    "selectable": true,
                                    "hoverAble": true,
                                    "cursor": "pointer",
                                    "eventHandlers": {}
                                }
                            },
                            {
                                "id": "seat-table-001-8",
                                "name": "8",
                                "position": {
                                    "x": 11.7,
                                    "y": 25.0
                                },
                                "status": "AVAILABLE",
                                "attributes": {
                                    "qualityScore": true,
                                    "premiumSeat": true,
                                    "diningTable": true,
                                    "waitService": true
                                },
                                "useAbsolutePosition": false,
                                "absolutePosition": undefined,
                                "ticketTypeId": "VIP",
                                "visualStyle": {
                                    "fillColor": "#FFD700",
                                    "strokeColor": "#8B4513",
                                    "strokeWidth": 2.0,
                                    "opacity": 1.0,
                                    "dashPattern": undefined,
                                    "visible": true,
                                    "zindex": 3
                                },
                                "interactiveProperties": {
                                    "draggable": false,
                                    "selectable": true,
                                    "hoverAble": true,
                                    "cursor": "pointer",
                                    "eventHandlers": {}
                                }
                            }
                        ]
                    },
                    {
                        "id": "table-002",
                        "name": "Table 2",
                        "position": {
                            "x": 200.0,
                            "y": 50.0
                        },
                        "dimensions": {
                            "width": 60.0,
                            "height": 70.0
                        },
                        "shape": "RECTANGLE",
                        "capacity": 6,
                        "seats": [
                            {
                                "id": "seat-table-002-1",
                                "name": "1",
                                "position": {
                                    "x": 33.3,
                                    "y": -15.0
                                },
                                "status": "AVAILABLE",
                                "attributes": {
                                    "qualityScore": true,
                                    "premiumSeat": true,
                                    "diningTable": true,
                                    "waitService": true
                                },
                                "useAbsolutePosition": false,
                                "absolutePosition": undefined,
                                "ticketTypeId": "VIP",
                                "visualStyle": {
                                    "fillColor": "#FFD700",
                                    "strokeColor": "#8B4513",
                                    "strokeWidth": 2.0,
                                    "opacity": 1.0,
                                    "dashPattern": undefined,
                                    "visible": true,
                                    "zindex": 3
                                },
                                "interactiveProperties": {
                                    "draggable": false,
                                    "selectable": true,
                                    "hoverAble": true,
                                    "cursor": "pointer",
                                    "eventHandlers": {}
                                }
                            },
                            {
                                "id": "seat-table-002-2",
                                "name": "2",
                                "position": {
                                    "x": 66.7,
                                    "y": -15.0
                                },
                                "status": "AVAILABLE",
                                "attributes": {
                                    "qualityScore": true,
                                    "premiumSeat": true,
                                    "diningTable": true,
                                    "waitService": true
                                },
                                "useAbsolutePosition": false,
                                "absolutePosition": undefined,
                                "ticketTypeId": "VIP",
                                "visualStyle": {
                                    "fillColor": "#FFD700",
                                    "strokeColor": "#8B4513",
                                    "strokeWidth": 2.0,
                                    "opacity": 1.0,
                                    "dashPattern": undefined,
                                    "visible": true,
                                    "zindex": 3
                                },
                                "interactiveProperties": {
                                    "draggable": false,
                                    "selectable": true,
                                    "hoverAble": true,
                                    "cursor": "pointer",
                                    "eventHandlers": {}
                                }
                            },
                            {
                                "id": "seat-table-002-3",
                                "name": "3",
                                "position": {
                                    "x": 115.0,
                                    "y": 30.0
                                },
                                "status": "AVAILABLE",
                                "attributes": {
                                    "qualityScore": true,
                                    "premiumSeat": true,
                                    "diningTable": true,
                                    "waitService": true
                                },
                                "useAbsolutePosition": false,
                                "absolutePosition": undefined,
                                "ticketTypeId": "VIP",
                                "visualStyle": {
                                    "fillColor": "#FFD700",
                                    "strokeColor": "#8B4513",
                                    "strokeWidth": 2.0,
                                    "opacity": 1.0,
                                    "dashPattern": undefined,
                                    "visible": true,
                                    "zindex": 3
                                },
                                "interactiveProperties": {
                                    "draggable": false,
                                    "selectable": true,
                                    "hoverAble": true,
                                    "cursor": "pointer",
                                    "eventHandlers": {}
                                }
                            },
                            {
                                "id": "seat-table-002-4",
                                "name": "4",
                                "position": {
                                    "x": 66.7,
                                    "y": 75.0
                                },
                                "status": "AVAILABLE",
                                "attributes": {
                                    "qualityScore": true,
                                    "premiumSeat": true,
                                    "diningTable": true,
                                    "waitService": true
                                },
                                "useAbsolutePosition": false,
                                "absolutePosition": undefined,
                                "ticketTypeId": "VIP",
                                "visualStyle": {
                                    "fillColor": "#FFD700",
                                    "strokeColor": "#8B4513",
                                    "strokeWidth": 2.0,
                                    "opacity": 1.0,
                                    "dashPattern": undefined,
                                    "visible": true,
                                    "zindex": 3
                                },
                                "interactiveProperties": {
                                    "draggable": false,
                                    "selectable": true,
                                    "hoverAble": true,
                                    "cursor": "pointer",
                                    "eventHandlers": {}
                                }
                            },
                            {
                                "id": "seat-table-002-5",
                                "name": "5",
                                "position": {
                                    "x": 33.3,
                                    "y": 75.0
                                },
                                "status": "AVAILABLE",
                                "attributes": {
                                    "qualityScore": true,
                                    "premiumSeat": true,
                                    "diningTable": true,
                                    "waitService": true
                                },
                                "useAbsolutePosition": false,
                                "absolutePosition": undefined,
                                "ticketTypeId": "VIP",
                                "visualStyle": {
                                    "fillColor": "#FFD700",
                                    "strokeColor": "#8B4513",
                                    "strokeWidth": 2.0,
                                    "opacity": 1.0,
                                    "dashPattern": undefined,
                                    "visible": true,
                                    "zindex": 3
                                },
                                "interactiveProperties": {
                                    "draggable": false,
                                    "selectable": true,
                                    "hoverAble": true,
                                    "cursor": "pointer",
                                    "eventHandlers": {}
                                }
                            },
                            {
                                "id": "seat-table-002-6",
                                "name": "6",
                                "position": {
                                    "x": -15.0,
                                    "y": 30.0
                                },
                                "status": "AVAILABLE",
                                "attributes": {
                                    "qualityScore": true,
                                    "premiumSeat": true,
                                    "diningTable": true,
                                    "waitService": true
                                },
                                "useAbsolutePosition": false,
                                "absolutePosition": undefined,
                                "ticketTypeId": "VIP",
                                "visualStyle": {
                                    "fillColor": "#FFD700",
                                    "strokeColor": "#8B4513",
                                    "strokeWidth": 2.0,
                                    "opacity": 1.0,
                                    "dashPattern": undefined,
                                    "visible": true,
                                    "zindex": 3
                                },
                                "interactiveProperties": {
                                    "draggable": false,
                                    "selectable": true,
                                    "hoverAble": true,
                                    "cursor": "pointer",
                                    "eventHandlers": {}
                                }
                            }
                        ]
                    }
                ]
            },
            {
                "id": "section-standard-001",
                "name": "Standard Section D",
                "type": "SEATED",
                "position": {
                    "x": 50.0,
                    "y": 700.0
                },
                "dimensions": {
                    "width": 1100.0,
                    "height": 200.0
                },
                "rotation": 5.0,
                "capacity": 400,
                "isArc": false,
                "arcProperties": undefined,
                "ticketTypeId": "Standard",
                "rows": [
                    {
                        "id": "row-standard-001-d",
                        "name": "Row D",
                        "position": {
                            "x": 100.0,
                            "y": 50.0
                        },
                        "isArc": false,
                        "arcProperties": undefined,
                        "seats": [
                            {
                                "id": "seat-standard-001-d-1",
                                "name": "1",
                                "position": {
                                    "x": 0.0,
                                    "y": 0.0
                                },
                                "status": "AVAILABLE",
                                "attributes": {
                                    "qualityScore": false,
                                    "premiumSeat": false,
                                    "hasArmrest": false,
                                    "extraLegroom": false
                                },
                                "useAbsolutePosition": false,
                                "absolutePosition": undefined,
                                "ticketTypeId": "Standard",
                                "visualStyle": {
                                    "fillColor": "#4ECDC4",
                                    "strokeColor": "#000000",
                                    "strokeWidth": 1.0,
                                    "opacity": 1.0,
                                    "dashPattern": undefined,
                                    "visible": true,
                                    "zindex": 3
                                },
                                "interactiveProperties": {
                                    "draggable": false,
                                    "selectable": true,
                                    "hoverAble": true,
                                    "cursor": "pointer",
                                    "eventHandlers": {}
                                }
                            },
                            {
                                "id": "seat-standard-001-d-2",
                                "name": "2",
                                "position": {
                                    "x": 40.0,
                                    "y": 0.0
                                },
                                "status": "DISABLED",
                                "attributes": {
                                    "qualityScore": false,
                                    "premiumSeat": false,
                                    "hasArmrest": false,
                                    "extraLegroom": false,
                                    "maintenance": true
                                },
                                "useAbsolutePosition": false,
                                "absolutePosition": undefined,
                                "ticketTypeId": "Standard",
                                "visualStyle": {
                                    "fillColor": "#CCCCCC",
                                    "strokeColor": "#666666",
                                    "strokeWidth": 1.0,
                                    "opacity": 0.5,
                                    "dashPattern": undefined,
                                    "visible": false,
                                    "zindex": 1
                                },
                                "interactiveProperties": {
                                    "draggable": false,
                                    "selectable": false,
                                    "hoverAble": false,
                                    "cursor": "default",
                                    "eventHandlers": {}
                                }
                            }
                        ]
                    }
                ],
                "tables": []
            },
            {
                "id": "section-standing-001",
                "name": "General Admission Standing",
                "type": "STANDING",
                "position": {
                    "x": 850.0,
                    "y": 200.0
                },
                "dimensions": {
                    "width": 300.0,
                    "height": 450.0
                },
                "rotation": 0.0,
                "capacity": 500,
                "isArc": false,
                "arcProperties": undefined,
                "ticketTypeId": "Economy",
                "rows": [],
                "tables": []
            }
        ]
    },
});

const createMockArcVenueData = (): VenueMap => ({
    "id": "venue-001",
    "name": "Grand Theater with Arc Seating",
    "dimensions": {
        "width": 1200,
        "height": 800
    },
    "stageConfig": {
        "id": "stage-001",
        "name": "Main Stage",
        "description": "Semi-circular main stage",
        "position": {
            "x": 600,
            "y": 100
        },
        "dimensions": {
            "width": 200,
            "height": 100
        },
        "rotation": 0,
        "shape": "SEMICIRCLE",
        "orientation": "SOUTH",
        "active": true,
        "elevation": 1.5
    },
    "seatConfig": {
        "seatTypeColors": {
            "premium": "#FFD700",
            "standard": "#87CEEB",
            "economy": "#DDA0DD"
        },
        "sections": [
            {
                "id": "section-orchestra",
                "name": "Orchestra Section",
                "type": "SEATED",
                "position": {
                    "x": 300,
                    "y": 250
                },
                "dimensions": {
                    "width": 600,
                    "height": 300
                },
                "rotation": 0,
                "capacity": 150,
                "isArc": true,
                "arcProperties": {
                    "centerX": 300,
                    "centerY": 0,
                    "radius": 200,
                    "startAngle": 30,
                    "endAngle": 150
                },
                "ticketTypeId": "premium",
                "rows": [
                    {
                        "id": "row-A",
                        "name": "Row A",
                        "position": {
                            "x": 0,
                            "y": 0
                        },
                        "isArc": true,
                        "arcProperties": {
                            "centerX": 300,
                            "centerY": 0,
                            "radius": 180,
                            "startAngle": 45,
                            "endAngle": 135
                        },
                        "seats": [
                            {
                                "id": "seat-A1",
                                "name": "1",
                                "position": {
                                    "x": 427.28,
                                    "y": 127.28
                                },
                                "status": "AVAILABLE",
                                "useAbsolutePosition": false,
                                "ticketTypeId": "premium",
                                "attributes": {
                                    "premiumSeat": true,
                                    "qualityScore": true
                                }
                            },
                            {
                                "id": "seat-A2",
                                "name": "2",
                                "position": {
                                    "x": 445.69,
                                    "y": 106.07
                                },
                                "status": "AVAILABLE",
                                "useAbsolutePosition": false,
                                "ticketTypeId": "premium",
                                "attributes": {
                                    "premiumSeat": true,
                                    "qualityScore": true
                                }
                            },
                            {
                                "id": "seat-A3",
                                "name": "3",
                                "position": {
                                    "x": 463.40,
                                    "y": 84.32
                                },
                                "status": "AVAILABLE",
                                "useAbsolutePosition": false,
                                "ticketTypeId": "premium",
                                "attributes": {
                                    "premiumSeat": true,
                                    "qualityScore": true
                                }
                            },
                            {
                                "id": "seat-A4",
                                "name": "4",
                                "position": {
                                    "x": 480.42,
                                    "y": 62.05
                                },
                                "status": "AVAILABLE",
                                "useAbsolutePosition": false,
                                "ticketTypeId": "premium",
                                "attributes": {
                                    "premiumSeat": true,
                                    "qualityScore": true
                                }
                            },
                            {
                                "id": "seat-A5",
                                "name": "5",
                                "position": {
                                    "x": 496.73,
                                    "y": 39.27
                                },
                                "status": "AVAILABLE",
                                "useAbsolutePosition": false,
                                "ticketTypeId": "premium",
                                "attributes": {
                                    "premiumSeat": true,
                                    "qualityScore": true
                                }
                            }
                        ]
                    },
                    {
                        "id": "row-B",
                        "name": "Row B",
                        "position": {
                            "x": 0,
                            "y": 0
                        },
                        "isArc": true,
                        "arcProperties": {
                            "centerX": 300,
                            "centerY": 0,
                            "radius": 220,
                            "startAngle": 40,
                            "endAngle": 140
                        },
                        "seats": [
                            {
                                "id": "seat-B1",
                                "name": "1",
                                "position": {
                                    "x": 441.42,
                                    "y": 141.42
                                },
                                "status": "AVAILABLE",
                                "useAbsolutePosition": false,
                                "ticketTypeId": "premium",
                                "attributes": {
                                    "premiumSeat": true,
                                    "qualityScore": true
                                }
                            },
                            {
                                "id": "seat-B2",
                                "name": "2",
                                "position": {
                                    "x": 464.85,
                                    "y": 114.85
                                },
                                "status": "AVAILABLE",
                                "useAbsolutePosition": false,
                                "ticketTypeId": "premium",
                                "attributes": {
                                    "premiumSeat": true,
                                    "qualityScore": true
                                }
                            },
                            {
                                "id": "seat-B3",
                                "name": "3",
                                "position": {
                                    "x": 487.38,
                                    "y": 87.38
                                },
                                "status": "AVAILABLE",
                                "useAbsolutePosition": false,
                                "ticketTypeId": "premium",
                                "attributes": {
                                    "premiumSeat": true,
                                    "qualityScore": true
                                }
                            },
                            {
                                "id": "seat-B4",
                                "name": "4",
                                "position": {
                                    "x": 509.02,
                                    "y": 59.02
                                },
                                "status": "AVAILABLE",
                                "useAbsolutePosition": false,
                                "ticketTypeId": "premium",
                                "attributes": {
                                    "premiumSeat": true,
                                    "qualityScore": true
                                }
                            },
                            {
                                "id": "seat-B5",
                                "name": "5",
                                "position": {
                                    "x": 529.78,
                                    "y": 29.78
                                },
                                "status": "AVAILABLE",
                                "useAbsolutePosition": false,
                                "ticketTypeId": "premium",
                                "attributes": {
                                    "premiumSeat": true,
                                    "qualityScore": true
                                }
                            }
                        ]
                    }
                ],
                "tables": []
            },
            {
                "id": "section-mezzanine",
                "name": "Mezzanine Crescent",
                "type": "SEATED",
                "position": {
                    "x": 200,
                    "y": 400
                },
                "dimensions": {
                    "width": 800,
                    "height": 200
                },
                "rotation": 0,
                "capacity": 120,
                "isArc": true,
                "arcProperties": {
                    "centerX": 400,
                    "centerY": -150,
                    "radius": 350,
                    "startAngle": 20,
                    "endAngle": 160
                },
                "ticketTypeId": "standard",
                "rows": [
                    {
                        "id": "row-C",
                        "name": "Row C",
                        "position": {
                            "x": 0,
                            "y": 0
                        },
                        "isArc": true,
                        "arcProperties": {
                            "centerX": 400,
                            "centerY": -150,
                            "radius": 320,
                            "startAngle": 25,
                            "endAngle": 155
                        },
                        "seats": [
                            {
                                "id": "seat-C1",
                                "name": "1",
                                "position": {
                                    "x": 290.64,
                                    "y": 136.0
                                },
                                "status": "AVAILABLE",
                                "useAbsolutePosition": false,
                                "ticketTypeId": "standard",
                                "attributes": {
                                    "qualityScore": true
                                }
                            },
                            {
                                "id": "seat-C2",
                                "name": "2",
                                "position": {
                                    "x": 320.0,
                                    "y": 127.28
                                },
                                "status": "AVAILABLE",
                                "useAbsolutePosition": false,
                                "ticketTypeId": "standard",
                                "attributes": {
                                    "qualityScore": true
                                }
                            },
                            {
                                "id": "seat-C3",
                                "name": "3",
                                "position": {
                                    "x": 349.36,
                                    "y": 118.56
                                },
                                "status": "AVAILABLE",
                                "useAbsolutePosition": false,
                                "ticketTypeId": "standard",
                                "attributes": {
                                    "qualityScore": true
                                }
                            },
                            {
                                "id": "seat-C4",
                                "name": "4",
                                "position": {
                                    "x": 378.72,
                                    "y": 109.84
                                },
                                "status": "AVAILABLE",
                                "useAbsolutePosition": false,
                                "ticketTypeId": "standard",
                                "attributes": {
                                    "qualityScore": true
                                }
                            },
                            {
                                "id": "seat-C5",
                                "name": "5",
                                "position": {
                                    "x": 408.08,
                                    "y": 101.12
                                },
                                "status": "AVAILABLE",
                                "useAbsolutePosition": false,
                                "ticketTypeId": "standard",
                                "attributes": {
                                    "qualityScore": true
                                }
                            },
                            {
                                "id": "seat-C6",
                                "name": "6",
                                "position": {
                                    "x": 437.44,
                                    "y": 92.4
                                },
                                "status": "AVAILABLE",
                                "useAbsolutePosition": false,
                                "ticketTypeId": "standard",
                                "attributes": {
                                    "qualityScore": true
                                }
                            }
                        ]
                    },
                    {
                        "id": "row-D",
                        "name": "Row D",
                        "position": {
                            "x": 0,
                            "y": 0
                        },
                        "isArc": true,
                        "arcProperties": {
                            "centerX": 400,
                            "centerY": -150,
                            "radius": 360,
                            "startAngle": 22,
                            "endAngle": 158
                        },
                        "seats": [
                            {
                                "id": "seat-D1",
                                "name": "1",
                                "position": {
                                    "x": 326.72,
                                    "y": 134.24
                                },
                                "status": "AVAILABLE",
                                "useAbsolutePosition": false,
                                "ticketTypeId": "standard",
                                "attributes": {
                                    "qualityScore": true
                                }
                            },
                            {
                                "id": "seat-D2",
                                "name": "2",
                                "position": {
                                    "x": 360.0,
                                    "y": 124.0
                                },
                                "status": "AVAILABLE",
                                "useAbsolutePosition": false,
                                "ticketTypeId": "standard",
                                "attributes": {
                                    "qualityScore": true
                                }
                            },
                            {
                                "id": "seat-D3",
                                "name": "3",
                                "position": {
                                    "x": 393.28,
                                    "y": 113.76
                                },
                                "status": "AVAILABLE",
                                "useAbsolutePosition": false,
                                "ticketTypeId": "standard",
                                "attributes": {
                                    "qualityScore": true
                                }
                            },
                            {
                                "id": "seat-D4",
                                "name": "4",
                                "position": {
                                    "x": 426.56,
                                    "y": 103.52
                                },
                                "status": "AVAILABLE",
                                "useAbsolutePosition": false,
                                "ticketTypeId": "standard",
                                "attributes": {
                                    "qualityScore": true
                                }
                            },
                            {
                                "id": "seat-D5",
                                "name": "5",
                                "position": {
                                    "x": 459.84,
                                    "y": 93.28
                                },
                                "status": "AVAILABLE",
                                "useAbsolutePosition": false,
                                "ticketTypeId": "standard",
                                "attributes": {
                                    "qualityScore": true
                                }
                            },
                            {
                                "id": "seat-D6",
                                "name": "6",
                                "position": {
                                    "x": 493.12,
                                    "y": 83.04
                                },
                                "status": "AVAILABLE",
                                "useAbsolutePosition": false,
                                "ticketTypeId": "standard",
                                "attributes": {
                                    "qualityScore": true
                                }
                            }
                        ]
                    }
                ],
                "tables": []
            },
            {
                "id": "section-balcony",
                "name": "Balcony Arc",
                "type": "SEATED",
                "position": {
                    "x": 150,
                    "y": 600
                },
                "dimensions": {
                    "width": 900,
                    "height": 150
                },
                "rotation": 0,
                "capacity": 90,
                "isArc": true,
                "arcProperties": {
                    "centerX": 450,
                    "centerY": -300,
                    "radius": 500,
                    "startAngle": 15,
                    "endAngle": 165
                },
                "ticketTypeId": "economy",
                "rows": [
                    {
                        "id": "row-E",
                        "name": "Row E",
                        "position": {
                            "x": 0,
                            "y": 0
                        },
                        "isArc": true,
                        "arcProperties": {
                            "centerX": 450,
                            "centerY": -300,
                            "radius": 480,
                            "startAngle": 18,
                            "endAngle": 162
                        },
                        "seats": [
                            {
                                "id": "seat-E1",
                                "name": "1",
                                "position": {
                                    "x": 148.2,
                                    "y": 148.8
                                },
                                "status": "AVAILABLE",
                                "useAbsolutePosition": false,
                                "ticketTypeId": "economy",
                                "attributes": {
                                    "qualityScore": false
                                }
                            },
                            {
                                "id": "seat-E2",
                                "name": "2",
                                "position": {
                                    "x": 196.4,
                                    "y": 135.6
                                },
                                "status": "AVAILABLE",
                                "useAbsolutePosition": false,
                                "ticketTypeId": "economy",
                                "attributes": {
                                    "qualityScore": false
                                }
                            },
                            {
                                "id": "seat-E3",
                                "name": "3",
                                "position": {
                                    "x": 244.6,
                                    "y": 122.4
                                },
                                "status": "AVAILABLE",
                                "useAbsolutePosition": false,
                                "ticketTypeId": "economy",
                                "attributes": {
                                    "qualityScore": false
                                }
                            },
                            {
                                "id": "seat-E4",
                                "name": "4",
                                "position": {
                                    "x": 292.8,
                                    "y": 109.2
                                },
                                "status": "AVAILABLE",
                                "useAbsolutePosition": false,
                                "ticketTypeId": "economy",
                                "attributes": {
                                    "qualityScore": false
                                }
                            },
                            {
                                "id": "seat-E5",
                                "name": "5",
                                "position": {
                                    "x": 341.0,
                                    "y": 96.0
                                },
                                "status": "AVAILABLE",
                                "useAbsolutePosition": false,
                                "ticketTypeId": "economy",
                                "attributes": {
                                    "qualityScore": false
                                }
                            }
                        ]
                    }
                ],
                "tables": []
            },
            {
                "id": "section-vip-tables",
                "name": "VIP Table Section",
                "type": "TABLE",
                "position": {
                    "x": 100,
                    "y": 200
                },
                "dimensions": {
                    "width": 150,
                    "height": 200
                },
                "rotation": 0,
                "capacity": 24,
                "isArc": false,
                "ticketTypeId": "premium",
                "rows": [],
                "tables": [
                    {
                        "id": "table-1",
                        "name": "Table 1",
                        "position": {
                            "x": 25,
                            "y": 25
                        },
                        "dimensions": {
                            "width": 100,
                            "height": 60
                        },
                        "shape": "ROUND",
                        "capacity": 8,
                        "seats": [
                            {
                                "id": "seat-T1-1",
                                "name": "1",
                                "position": {
                                    "x": 50,
                                    "y": 0
                                },
                                "status": "AVAILABLE",
                                "useAbsolutePosition": false,
                                "ticketTypeId": "premium",
                                "attributes": {
                                    "premiumSeat": true,
                                    "qualityScore": true
                                }
                            },
                            {
                                "id": "seat-T1-2",
                                "name": "2",
                                "position": {
                                    "x": 85.36,
                                    "y": 14.64
                                },
                                "status": "AVAILABLE",
                                "useAbsolutePosition": false,
                                "ticketTypeId": "premium",
                                "attributes": {
                                    "premiumSeat": true,
                                    "qualityScore": true
                                }
                            },
                            {
                                "id": "seat-T1-3",
                                "name": "3",
                                "position": {
                                    "x": 85.36,
                                    "y": 45.36
                                },
                                "status": "AVAILABLE",
                                "useAbsolutePosition": false,
                                "ticketTypeId": "premium",
                                "attributes": {
                                    "premiumSeat": true,
                                    "qualityScore": true
                                }
                            },
                            {
                                "id": "seat-T1-4",
                                "name": "4",
                                "position": {
                                    "x": 50,
                                    "y": 60
                                },
                                "status": "AVAILABLE",
                                "useAbsolutePosition": false,
                                "ticketTypeId": "premium",
                                "attributes": {
                                    "premiumSeat": true,
                                    "qualityScore": true
                                }
                            }
                        ]
                    },
                    {
                        "id": "table-2",
                        "name": "Table 2",
                        "position": {
                            "x": 25,
                            "y": 115
                        },
                        "dimensions": {
                            "width": 100,
                            "height": 60
                        },
                        "shape": "ROUND",
                        "capacity": 8,
                        "seats": [
                            {
                                "id": "seat-T2-1",
                                "name": "1",
                                "position": {
                                    "x": 50,
                                    "y": 0
                                },
                                "status": "AVAILABLE",
                                "useAbsolutePosition": false,
                                "ticketTypeId": "premium",
                                "attributes": {
                                    "premiumSeat": true,
                                    "qualityScore": true
                                }
                            },
                            {
                                "id": "seat-T2-2",
                                "name": "2",
                                "position": {
                                    "x": 85.36,
                                    "y": 14.64
                                },
                                "status": "AVAILABLE",
                                "useAbsolutePosition": false,
                                "ticketTypeId": "premium",
                                "attributes": {
                                    "premiumSeat": true,
                                    "qualityScore": true
                                }
                            },
                            {
                                "id": "seat-T2-3",
                                "name": "3",
                                "position": {
                                    "x": 85.36,
                                    "y": 45.36
                                },
                                "status": "AVAILABLE",
                                "useAbsolutePosition": false,
                                "ticketTypeId": "premium",
                                "attributes": {
                                    "premiumSeat": true,
                                    "qualityScore": true
                                }
                            },
                            {
                                "id": "seat-T2-4",
                                "name": "4",
                                "position": {
                                    "x": 50,
                                    "y": 60
                                },
                                "status": "AVAILABLE",
                                "useAbsolutePosition": false,
                                "ticketTypeId": "premium",
                                "attributes": {
                                    "premiumSeat": true,
                                    "qualityScore": true
                                }
                            }
                        ]
                    }
                ]
            }
        ]
    }
});

export {
    createMockVenueData,
    createMockArcVenueData
};