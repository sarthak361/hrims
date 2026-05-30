package com.hti.response;

/**
 * PaginatedResponse.java
 *
 * This class is part of the Messenger Client Service.
 *
 * @author VINOD YOGI
 * @designation Senior Team Lead
 * @contact rajguruv737@gmail.com | +91 6263815568
 * @since 2026-03-25
 * @version 1.0
 */

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginatedResponse<T> {
	private List<T> content;
	private int page;
	private int size;
	private long totalElements;
	private int totalPages;
	private boolean last;
}
