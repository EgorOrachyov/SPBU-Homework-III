package Application.Concurrent;

public abstract class IList<Element> {

    /**
     * Add element in the list
     *
     * @param element to store in the list
     */
    public abstract void add(Element element);

    /**
     * Return element via index or null if the
     * list does not contain the elements with that indices
     *
     * @param index to find the element
     * @return Element if it is found or otherwise null
     */
    public abstract Element get(int index);

    /**
     * Cheks whether the list contains this element
     * and returns true or othervise false
     *
     * @param element to check in the list
     * @return true if contain this element
     */
    public abstract boolean contains(Element element);

    /**
     * Search the element in the list by special finder
     * terms to get that
     *
     * @param finder Object used to find the needed element
     * @return Element if it is found or otherwise null
     */
    public abstract Element find(IFinder<Element> finder);

}
