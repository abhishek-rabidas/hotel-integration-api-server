package com.github.abhishek_rabidas.Hotel_Integration_API.dao;

import com.github.abhishek_rabidas.Hotel_Integration_API.models.HrmsUser;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends BaseRepository<HrmsUser, Long> {
}
