const rectangle = {
    'venueType': 'INDOOR',
    'seatTypeColors': {
        'VIP': '#FFD700',
        'REGULAR': '#90EE90',
        'ECONOMY': '#ADD8E6'
    },
    'seatMap': {
        '@type': 'RectangleSeatMap',
        'totalRows': 3,
        'seatsPerRow': 5,
        'sections': [
            {
                'name': 'Main Section',
                'seatType': 'VIP',
                'rows': [
                    {
                        'rowName': 'A',
                        'seatCount': 5,
                        'price': 150.0,
                        'seats': [
                            {'seatId': 'A1', 'x': 0, 'y': 0, 'ticketTypeId': 'VIP', 'price': 150.0},
                            {'seatId': 'A2', 'x': 1, 'y': 0, 'ticketTypeId': 'VIP', 'price': 150.0},
                            {'seatId': 'A3', 'x': 2, 'y': 0, 'ticketTypeId': 'VIP', 'price': 150.0},
                            {'seatId': 'A4', 'x': 3, 'y': 0, 'ticketTypeId': 'VIP', 'price': 150.0},
                            {'seatId': 'A5', 'x': 4, 'y': 0, 'ticketTypeId': 'VIP', 'price': 150.0}
                        ]
                    }
                ]
            }
        ]
    },
    'stage': {
        'label': 'Artistic Center Stage',
        'stageX': 100,
        'stageY': 75,
        'stageWidth': 250,
        'stageHeight': 120,
        'shape': 'CUSTOM',
        'orientation': 'DOWN',
        'svgPath': 'M10 10 H 90 V 90 H 10 L 10 10'
    }
};

const circle = {
    'venueType': 'OUTDOOR',
    'seatTypeColors': {
        'Standard': '#00FF00'
    },
    'seatMap': {
        '@type': 'CircleSeatMap',
        'centerX': 250,
        'centerY': 250,
        'radius': 200,
        'angleStep': 15.0,
        'sections': [
            {
                'name': 'Circle Zone',
                'seatType': 'Standard',
                'rows': [
                    {
                        'rowName': 'C1',
                        'seatCount': 24,
                        'price': 50.0,
                        'seats': [
                            {
                                'seatId': 'C1-1',
                                'x': 300,
                                'y': 100,
                                'ticketTypeId': 'Standard',
                                'price': 50.0
                            }
                        ]
                    }
                ]
            }
        ]
    },
    'stage': {
        'label': 'Artistic Center Stage',
        'stageX': 100,
        'stageY': 75,
        'stageWidth': 250,
        'stageHeight': 120,
        'shape': 'CUSTOM',
        'orientation': 'DOWN',
        'svgPath': 'M10 10 H 90 V 90 H 10 L 10 10'
    }
};

const point = {
    'venueType': 'INDOOR',
    'seatTypeColors': {
        'Balcony': '#8A2BE2'
    },
    'seatMap': {
        '@type': 'PointSeatMap',
        'points': [
            {
                'x': 100,
                'y': 50
            },
            {
                'x': 120,
                'y': 60
            }
        ],
        'sections': [
            {
                'name': 'Balcony',
                'seatType': 'Balcony',
                'rows': [
                    {
                        'rowName': 'B1',
                        'seatCount': 2,
                        'price': 70.0,
                        'seats': [
                            {
                                'seatId': 'B1-1',
                                'x': 100,
                                'y': 50,
                                'ticketTypeId': 'Balcony',
                                'price': 70.0
                            }
                        ]
                    }
                ]
            }
        ]
    },
    'stage': {
        'label': 'Artistic Center Stage',
        'stageX': 100,
        'stageY': 75,
        'stageWidth': 250,
        'stageHeight': 120,
        'shape': 'CUSTOM',
        'orientation': 'DOWN',
        'svgPath': 'M10 10 H 90 V 90 H 10 L 10 10'
    }
};

const custom = {
    'venueType': 'OUTDOOR',
    'seatTypeColors': {
        'PREMIUM': '#FFA07A',
        'STANDARD': '#D3D3D3'
    },
    'seatMap': {
        '@type': 'CustomSeatMap',
        'svgPath': '',
        'zones': [
            {
                'name': 'Zone A',
                'seatType': 'PREMIUM',
                'zoneX': 100,
                'zoneY': 50,
                'width': 200,
                'height': 150,
                'area': {
                    '@type': 'CircleSeatMap',
                    'centerX': 100,
                    'centerY': 100,
                    'radius': 80,
                    'angleStep': 20.0,
                    'sections': [
                        {
                            'name': 'Premium Circle',
                            'seatType': 'PREMIUM',
                            'rows': [
                                {
                                    'rowName': 'CircleRow',
                                    'seatCount': 18,
                                    'price': 200.0,
                                    'seats': []
                                }
                            ]
                        }
                    ]
                }
            }
        ],
    },
    'stage': {
        'label': 'Artistic Center Stage',
        'stageX': 100,
        'stageY': 75,
        'stageWidth': 250,
        'stageHeight': 120,
        'shape': 'CUSTOM',
        'orientation': 'DOWN',
        'svgPath': 'M10 10 H 90 V 90 H 10 L 10 10'
    }
};