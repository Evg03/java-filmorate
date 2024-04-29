package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class MpaServiceImpl implements MpaService {

    MpaStorage mpaStorage;

    @Override
    public Mpa getMpa(int mpaId) {
        Mpa mpa = mpaStorage.getMpa(mpaId);
        if (mpa == null) {
            log.warn("Mpa с id = {} не существует", mpaId);
            throw new MpaNotFoundException(String.format("Mpa c id = %s не существует", mpaId));
        }
        return mpa;
    }

    @Override
    public List<Mpa> getAllMpa() {
        List<Mpa> allMpa = mpaStorage.getAllMpa();
        log.info("Количество mpa = {}", allMpa.size());
        return allMpa;
    }
}
