package list;

import java.util.Comparator;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

/**
 * This Solar System is implemented as a doubly linked list. Planets may be
 * added to or removed from a Solar System. They may also be sorted by their
 * distance from their sun in AU, the length of their day in hours, or their
 * number of moons.
 *
 */
public class SolarSystem {

	private static class Node {
		Planet planet;
		Node before, after;

		/**
		 * @param p, a planet, must not be null
		 */
		Node(Planet p, Node b, Node n) {
			planet = p;
			before = b;
			after = n;
		}
	}

	private Node head, tail;

	public SolarSystem() {
		head = tail = null;
	}

	/**
	 * Add a planet to the end of the list
	 * 
	 * @param p, planet to be added
	 */
	public void add(Planet p) {
		if (head == null)
			tail = head = new Node(p, null, null);
		else
			tail = tail.after = new Node(p, tail, tail.after);
	}

	/**
	 * Remove a planet from the Solar System
	 * 
	 * @param p, planet to be removed
	 * @return true if the removal occurred, or false otherwise
	 */
	public boolean remove(Planet p) {
		if (p == null || head == null)
			return false;

		// 1. If the element is at the head
		Node planet = getNode(p);
		if (planet == null) {
			return false;
		} else if (head.after == null) {
			head = null;
			tail = null;
		} else if (p == head.planet) {
			Node copyHead = head;
			Node afterHead = head.after;
			copyHead.after = null;
			head = afterHead;
			afterHead.before = null;
		}

		// 2. If the element is at the tail
		else if (p == tail.planet) {
			tail = tail.before;
			tail.after = null;
		}

		// 3. If the element is anywhere else
		else {
			Node planetBefore = planet.before;
			Node planetAfter = planet.after;
			planetBefore.after = planetAfter;
			planetAfter.before = planetBefore;
			planet.before = null;
			planet.after = null;

		}

		return true;
	}

	private Node getNode(Planet p) {
		for (Node n = head; n != null; n = n.after)
			if (n.planet.equals(p))
				return n;

		return null;
	}

	/**
	 * Sort using the given comparator
	 * 
	 * @param comparator
	 */
	public void sort(Comparator<Planet> comparator) {
		if (head == null || head.after == null)
			return;
		bubbleSort(comparator);
	}

	/**
	 * Using bubbleSort, sort the elements by the given comparator
	 * 
	 * @precondition assume that there is at least one element and therefore head is
	 *               not null
	 * 
	 * @param comparator describes how elements should be ordered Note: Comparator
	 *                   usage comparator.compare(t1,t2) returns an integer if t1
	 *                   comes before t2, returns a negative number if t1 comes
	 *                   after t2, returns a positive number if t1 and t2 are
	 *                   interchangeable, returns 0
	 */
	private void bubbleSort(Comparator<Planet> comparator) {
		boolean swapped = false;
		Node current = head;
		while (current.after != null) {
			// compare current and the after element
			// if they are out of order then swap
			if (comparator.compare(current.planet, current.after.planet) > 0) {
				swap(current);
				swapped = true; // swap if there not in the right order
			} else {
				current = current.after;
			}

		}
		if (swapped)
			bubbleSort(comparator);
	}

	/**
	 * Swaps the given current node with the following node
	 * 
	 * @param current, current node to swap with current.after
	 */
	private void swap(Node current) {
		Node after = current.after;

		if (after.after != null)
			after.after.before = current;
		if (current.before != null)
			current.before.after = after;
		current.after = after.after;
		after.before = current.before;
		after.after = current;
		current.before = after;
		if (current == head)
			head = after;
		if (after == tail)
			tail = current;
	}

	/**
	 * Sorts the planets by their distance from their sun in AU Note: 1 AU
	 * (astronomical unit) is the distance from the Earth to the Sun
	 */
	public void sortByDistance() {
		sort(SolarSystem.distanceComparator());
	}

	private static Comparator<Planet> distanceComparator() {
		return new Comparator<Planet>() {
			@Override
			public int compare(Planet p1, Planet p2) {
				return (int) Math.round(p1.getDistance() * 10 - p2.getDistance() * 10);
			}
		};
	}

	/**
	 * Sorts the planets by the length of their day in Earth hours
	 */
	public void sortByDayLength() {

		sort(SolarSystem.dayLengthComparator());
	}

	private static Comparator<Planet> dayLengthComparator() {
		return new Comparator<Planet>() {

			// TODO
			// Make this comparator actually compare these planets by
			// their day length
			public int compare(Planet p1, Planet p2) {
				return ((int) p1.getDayInHours() - (int) p2.getDayInHours()); // comparator
			}

		};
	}

	/**
	 * Sorts the planets by their number of moons
	 */
	public void sortByNumMoons() {
		sort(SolarSystem.numMoonsComparator());
	}

	private static Comparator<Planet> numMoonsComparator() {
		return new Comparator<Planet>() {
			@Override
			public int compare(Planet p1, Planet p2) {
				return (int) Math.round(p1.getMoons() - p2.getMoons());
			}
		};
	}

	/**
	 * Creates a canvas to reflect the state of the Solar System
	 * 
	 * @return
	 */
	public JPanel makeCanvas() {
		return new SolarSystemCanvas();
	}

	private class SolarSystemCanvas extends JPanel {
		private static final long serialVersionUID = -2226594746308463675L;

		private static final int BOX_WIDTH = 200, BOX_HEIGHT = 130, SECTION_HEIGHT = 20;
		private static final int X_SPACE = 30;
		private static final int Y = 10;
		private static final int ARROWSIZE = 5;

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Node cursor = head;
			int x = 10;
			while (cursor != null) {
				paintBox(g, x, cursor);
				x += BOX_WIDTH + X_SPACE;
				cursor = cursor.after;
			}
		}

		private void paintBox(Graphics g, int x, Node cursor) {
			paintRectangle(g, x, null);
			paintText(g, x, Y + SECTION_HEIGHT - 5, cursor.planet.getName());
			paintText(g, x, Y + SECTION_HEIGHT * 2 - 5, "Distance From Sun (AU): " + cursor.planet.getDistance());
			paintText(g, x, Y + SECTION_HEIGHT * 3 - 5, "Day Length (hr): " + cursor.planet.getDayInHours());
			paintText(g, x, Y + SECTION_HEIGHT * 4 - 5, "Number of Moons: " + cursor.planet.getMoons());
			if (cursor.before != null)
				paintPrevPointer(g, x);
			if (cursor.after != null)
				paintNextPointer(g, x);
		}

		private void paintRectangle(Graphics g, int x, Color color) {
			if (color != null) {
				Color previous = g.getColor();
				g.setColor(color);
				g.fillRect(x, Y, BOX_WIDTH, BOX_HEIGHT);
				g.setColor(previous);
			}
			g.drawRect(x, Y, BOX_WIDTH, BOX_HEIGHT);
			for (int y2 = SECTION_HEIGHT; y2 < BOX_HEIGHT; y2 += SECTION_HEIGHT) {
				g.drawLine(x, Y + y2, x + BOX_WIDTH, Y + y2);
			}
		}

		private void paintText(Graphics g, int x, int y, String text) {
			FontMetrics fm = getFontMetrics(getFont());
			int textWidth = fm.stringWidth(text);
			if (textWidth > BOX_WIDTH) {
				int textX = x + (BOX_WIDTH - 10) / 2;
				g.drawString("...", textX, y);
			} else {
				int textX = x + (BOX_WIDTH - textWidth) / 2;
				g.drawString(text, textX, y);
			}
		}

		private void paintNextPointer(Graphics g, int x) {
			int startX = x + BOX_WIDTH / 2;
			int startY = Y + BOX_HEIGHT - SECTION_HEIGHT / 2 - SECTION_HEIGHT / 3;
			int endX = x + BOX_WIDTH + X_SPACE;
			int[] pointx = { endX, endX - ARROWSIZE, endX - ARROWSIZE };
			int[] pointy = { startY, startY + ARROWSIZE, startY - ARROWSIZE };
			g.drawLine(startX, startY, endX, startY);
			g.fillPolygon(pointx, pointy, pointx.length);
		}

		private void paintPrevPointer(Graphics g, int x) {
			int startX = x - X_SPACE;
			int startY = Y + BOX_HEIGHT - SECTION_HEIGHT - SECTION_HEIGHT / 2 - SECTION_HEIGHT / 3;
			int endX = x + BOX_WIDTH / 2;
			int[] pointx = { startX, startX + ARROWSIZE, startX + ARROWSIZE };
			int[] pointy = { startY, startY + ARROWSIZE, startY - ARROWSIZE };
			g.drawLine(startX, startY, endX, startY);
			g.fillPolygon(pointx, pointy, pointx.length);
		}
	}

}
