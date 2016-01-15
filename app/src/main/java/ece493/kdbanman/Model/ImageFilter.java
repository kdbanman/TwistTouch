package ece493.kdbanman.Model;

import java.util.ArrayList;
import java.util.List;

import ece493.kdbanman.Observable;

/**
 * Implementation of the logic and state that is common to kernel-based convolution
 * filters.  Works with the Filterable class and FilterKernel interface.
 *
 * Created by kdbanman on 1/13/16.
 */
public class ImageFilter extends Observable {

    private int kernelSize = 3;

    private FilterKernelType filterKernelType = FilterKernelType.mean;

    private List<BackgroundFilterTask> backgroundTasks = new ArrayList<>();

    public boolean isFilterRunning() {
        for (BackgroundFilterTask task : backgroundTasks) {
            if (task.isTaskRunning()) {
                return true;
            }
        }
        return false;
    }

    public boolean isTaskStopping() {
        for (BackgroundFilterTask task : backgroundTasks) {
            if (task.isTaskRunning() && task.isTaskCancelled()) {
                return true;
            }
        }
        return false;
    }

    public void cancelBackgroundFilterTasks() {
        for (BackgroundFilterTask task : backgroundTasks) {
            task.cancelTask();
        }
    }

    public void setKernelType(FilterKernelType type) {
        filterKernelType = type;

        notifyObservers();
    }

    public FilterKernelType getKernelType() {
        return filterKernelType;
    }

    /**
     * @return The edge length in pixels for the square convolution window.
     */
    public final int getKernelSize() {
        return kernelSize;
    }

    /**
     * Sets the edge length in pixels for the square convolution window.
     *
     * @param kernelSize The new convolution window size.
     * @throws IllegalArgumentException if the kernelSize is less than 3 or odd.
     */
    public final void setKernelSize(int kernelSize) {
        if (kernelSize < 3) {
            throw new IllegalArgumentException("Kernel size must be 3 or larger.");
        }
        if (kernelSize % 2 == 0) {
            throw new IllegalArgumentException("Kernel size must be odd.");
        }

        this.kernelSize = kernelSize;

        notifyObservers();
    }

    /**
     * Applies a convolution filter to a filterable image in a background task.
     *
     * @param image The image to be filtered.
     */
    public void backgroundFilterImage(Filterable image) {
        if (image == null) {
            throw new IllegalArgumentException("Image must exist to be filtered.");
        }

        FilterKernel filterKernel;
        switch (filterKernelType) {
            case mean:
                filterKernel = new MeanFilterKernel(kernelSize);
                break;
            case median:
                filterKernel = new MedianFilterKernel(kernelSize);
                break;
            default:
                throw new IllegalArgumentException("Unrecognized filter kernel type requested in background task.");
        }

        BackgroundFilterTask task = new BackgroundFilterTask(filterKernel, this);
        backgroundTasks.add(task);
        task.execute(image);
    }
}