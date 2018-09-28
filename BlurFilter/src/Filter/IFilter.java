package Filter;

import Filter.Tools.Image;

public interface IFilter {

    public void apply(Image source, Image result);

    public Image apply(Image source);

}
