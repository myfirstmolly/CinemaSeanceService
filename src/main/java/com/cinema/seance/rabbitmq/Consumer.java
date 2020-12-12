package com.cinema.seance.rabbitmq;

import com.cinema.seance.model.Seance;
import com.cinema.seance.service.SeancesService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Consumer {

    @Autowired
    SeancesService seancesService;

    @RabbitListener(queues = "${rabbitmq.queue}")
    public void consume(Seance seance) {
        System.out.println(seance.toString());
        seancesService.addSeance(seance);
    }
}