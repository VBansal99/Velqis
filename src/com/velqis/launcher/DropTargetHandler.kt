package com.velqis.launcher

import android.content.ComponentName
import android.view.View
import com.velqis.launcher.BaseDraggingActivity.EVENT_RESUMED
import com.velqis.launcher.DropTarget.DragObject
import com.velqis.launcher.LauncherConstants.ActivityCodes
import com.velqis.launcher.SecondaryDropTarget.DeferredOnComplete
import com.velqis.launcher.dragndrop.DragLayer
import com.velqis.launcher.logging.StatsLogManager.LauncherEvent
import com.velqis.launcher.model.data.ItemInfo
import com.velqis.launcher.model.data.LauncherAppWidgetInfo
import com.velqis.launcher.util.IntSet
import com.velqis.launcher.util.PendingRequestArgs
import com.velqis.launcher.views.Snackbar

/**
 * Handler class for drop target actions that require modifying or interacting with launcher.
 *
 * This class is created by Launcher and provided the instance of launcher when created, which
 * allows us to decouple drop target controllers from Launcher to enable easier testing.
 */
class DropTargetHandler(launcher: Launcher) {
    val mLauncher: Launcher = launcher

    fun onDropAnimationComplete() {
        mLauncher.stateManager.goToState(LauncherState.NORMAL)
    }

    fun onSecondaryTargetCompleteDrop(target: ComponentName?, d: DragObject) {
        when (val dragSource = d.dragSource) {
            is DeferredOnComplete -> {
                val deferred: DeferredOnComplete = dragSource
                if (d.dragSource is SecondaryDropTarget.DeferredOnComplete) {
                    target?.let {
                        deferred.mPackageName = it.packageName
                        mLauncher.addEventCallback(EVENT_RESUMED) { deferred.onLauncherResume() }
                    } ?: deferred.sendFailure()
                }
            }
        }
    }

    fun reconfigureWidget(widgetId: Int, info: ItemInfo) {
        mLauncher.setWaitingForResult(PendingRequestArgs.forWidgetInfo(widgetId, null, info))
        mLauncher.appWidgetHolder.startConfigActivity(
            mLauncher,
            widgetId,
            ActivityCodes.REQUEST_RECONFIGURE_APPWIDGET,
        )
    }

    fun getViewUnderDrag(info: ItemInfo): View? {
        return if (
            info is LauncherAppWidgetInfo &&
            info.container == LauncherSettings.Favorites.CONTAINER_DESKTOP &&
            mLauncher.workspace.dragInfo != null
        ) {
            mLauncher.workspace.dragInfo.cell
        } else null
    }

    fun prepareToUndoDelete() {
        mLauncher.modelWriter.prepareToUndoDelete()
    }

    fun onDeleteComplete(item: ItemInfo) {
        removeItemAndStripEmptyScreens(null /* view */, item)
        var pageItem: ItemInfo = item
        if (item.container <= 0) {
            val v = mLauncher.workspace.getHomescreenIconByItemId(item.container)
            v?.let { pageItem = v.tag as ItemInfo }
        }
        val pageIds =
            if (pageItem.container == LauncherSettings.Favorites.CONTAINER_DESKTOP)
                IntSet.wrap(pageItem.screenId)
            else mLauncher.workspace.currentPageScreenIds
        val onUndoClicked = Runnable {
            mLauncher.setPagesToBindSynchronously(pageIds)
            mLauncher.modelWriter.abortDelete()
            mLauncher.statsLogManager.logger().log(LauncherEvent.LAUNCHER_UNDO)
        }

        Snackbar.show(
            mLauncher,
            R.string.item_removed,
            R.string.undo,
            mLauncher.modelWriter::commitDelete,
            onUndoClicked,
        )
    }

    fun onAccessibilityDelete(view: View?, item: ItemInfo, announcement: CharSequence) {
        removeItemAndStripEmptyScreens(view, item)
        mLauncher.dragLayer.announceForAccessibility(announcement)
    }

    fun getDragLayer(): DragLayer {
        return mLauncher.dragLayer
    }

    fun onClick(buttonDropTarget: ButtonDropTarget) {
        mLauncher.accessibilityDelegate.handleAccessibleDrop(buttonDropTarget, null, null)
    }

    private fun removeItemAndStripEmptyScreens(view: View?, item: ItemInfo) {
        // Remove the item from launcher and the db, we can ignore the containerInfo in this call
        // because we already remove the drag view from the folder (if the drag originated from
        // a folder) in Folder.beginDrag()
        mLauncher.removeItem(view, item, true /* deleteFromDb */, "removed by accessibility drop")
        mLauncher.workspace.stripEmptyScreens()
    }
}
