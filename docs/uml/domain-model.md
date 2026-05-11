---
config:
  layout: elk
  theme: base
  look: neo
  fontFamily: '''Inter Variable'', sans-serif'
  themeVariables:
    fontFamily: '''Inter Variable'', sans-serif'
title: Citylogic - Pure Domain Model (Conceptual Perspective)
---
classDiagram
direction TB
    class City {
	    Name
	    Total Budget
	    Total Population
	    Total Pollution
    }

    class Goal {
	    Description
	    Target Value
	    Current Progress
	    Is Completed
    }

    class Policy {
	    Name
	    Tax Rate Multiplier
	    Pollution Multiplier
    }

    class CityEvent {
	    Type
    }

    class UrbanGrid {
	    Width
	    Height
    }

    class Cell {
	    X Coordinate
	    Y Coordinate
    }

    class PowerPlant {
	    Energy Produced
	    Pollution Generated
    }

    class Park {
	    Pollution Reduction
    }

    class Road {
	    Connectivity Status
    }

    class FireStation {
	    Protection Radius
    }

    class Infrastructure {
	    Name
	    Construction Cost
	    Maintenance Cost
    }

    class Residential {
	    Current Population
	    Rent
	    Capacity
	    Local Happiness
    }

    class Industrial {
	    Base Tax Revenue
	    Pollution Generated
    }

	<<abstract>> CityEvent
	<<abstract>> Infrastructure

    Policy "1" -- "1" City : governs >
    City "1" *-- "*" Goal : pursues >
    City "1" o-- "*" CityEvent : undergoes >
    City "1" *-- "1" UrbanGrid : contains >
    UrbanGrid "1" *-- "*" Cell : is composed of >
    Cell "1" o-- "0..1" Infrastructure : hosts >
    Infrastructure <|-- Residential
    Infrastructure <|-- Industrial
    Infrastructure <|-- PowerPlant
    Infrastructure <|-- Park
    Infrastructure <|-- Road
    Infrastructure <|-- FireStation
