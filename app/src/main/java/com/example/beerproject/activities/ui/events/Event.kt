package com.example.beerproject.activities.ui.events

class Event {
    var id: Number? = null
    var name: String? = null
    var description: String? = null
    var date: String? = null

    constructor() {}

    constructor(id: Int?, name: String?, description: String?, date: String?) {
        this.id = id
        this.name = name
        this.description = description
        this.date = date
    }
}

