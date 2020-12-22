package com.example.beerproject.activities.ui.map

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.beerproject.activities.ui.map.models.Place

class MapViewModel : ViewModel() {

    public val _place = MutableLiveData<List<Place>>().apply{
        value = listOf(
            Place(
                "Хоспер-ХОПС-ПАБ",
                "улица Колесникова 38, Минск. Открыто 11:00-00:00",
                53.92164000035742,
                27.465255547841455
            ),
            Place(
                "Пивная № 1",
                "ул. Кальварийская 44, 220079, Минск. Открыто 11:00-00:00",
                53.915169747790905,
                27.514007375378394
            ),
            Place(
                "CUBA BAR",
                "ул. Мележа 5/1, Минск. Открыто 11:00-00:00",
                53.946702759508966,
                27.594344893995608
            ),
            Place(
                "План Б",
                "Дом прессы, вуліца Багдана Хмяльніцкага 10А, Мінск. Открыто 11:00-00:00",
                53.93336477763946,
                27.595031539453878
            ),
            Place(
                "ID bar",
                "ул. Захарова 19, Минск 220034. Открыто 11:00-00:00",
                53.91719180942732,
                27.578552048455474
            ),
        )
    }
}