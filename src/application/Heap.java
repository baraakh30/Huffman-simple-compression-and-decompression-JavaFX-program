package application;

public class Heap {

	Node[] heap;
	int maxSize;
	int size;

	public Heap(Node[] arr, int maxSize, int size) {
		heap = arr;
		this.maxSize = maxSize;
		this.size = size;
		buildHeap(heap, size);
	}

	// n is size of heap, i root of subtree which is an index in the heap
	public void heapify(Node arr[], int n, int i) {
		int min = i;
		int right = 2 * i + 2;
		int left = 2 * i + 1;

		// Left is less than min, set min = left
		if (left < n && arr[left].compareTo(arr[min]) == -1) {
			min = left;
		}

		// Right is less than min, set min = right
		if (right < n && arr[right].compareTo(arr[min]) == -1) {
			min = right;
		}

		// If the min isn't the root then a change happened and re-heapify recursively
		if (min != i) {

			// Make changes in heap
			Node temp = arr[i];
			arr[i] = arr[min];
			arr[min] = temp;

			// Heapify subtree
			heapify(arr, n, min);
		}
	}

	public void backHeapify(Node[] arr, int n, int i) {
		// Locate parent
		int parent = (i - 1) / 2;

		if (arr[parent].compareTo(new Node(0, -1, false)) == 1) {
			// Min heap, swap if one of the child nodes are less than the parent
			if (arr[i].compareTo(arr[parent]) == 1) {

				Node temp = arr[parent];
				arr[parent] = arr[i];
				arr[i] = temp;

				// Reheapify after change
				heapify(arr, n, parent);
			}
		}
	}

	// Build min heap
	public void buildHeap(Node[] arr, int n) {
		// Last non-leaf node
		int startIndex = (n / 2) - 1;

		// Starting from last non-leaf node, start reheapifying from the bottom up
		for (int i = startIndex; i >= 0; i--) {
			heapify(arr, n, i);
		}
	}

	// Returns min and deletes it
	public Node deleteMin() {

		if (size == 0) {
			return new Node(0, -1, false);
		}

		// Save root to return it
		Node min = heap[0];

		// Remove root from heap and replace it with the last node
		heap[0] = heap[size - 1];
		size--;

		// Re-heapify after changes
		heapify(heap, size, 0);

		return min;
	}

	public boolean add(Node newNode) {

		// If the heap is full, return false
		if (size == maxSize) {
			return false;
		}

		// Add node to end of heap array
		heap[size] = newNode;
		size++;

		// Re-heapify
		backHeapify(heap, size, size - 1);
		return true;
	}

	public void printHeapAsArray() {
		for (int i = 0; i < size; i++) {
			System.out.print(heap[i] + " ");
		}
	}
}
