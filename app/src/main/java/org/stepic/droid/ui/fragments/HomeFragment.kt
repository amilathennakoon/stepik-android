package org.stepic.droid.ui.fragments

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_home.*
import org.stepic.droid.R
import org.stepic.droid.base.App
import org.stepic.droid.base.FragmentBase
import org.stepic.droid.core.presenters.ContinueCoursePresenter
import org.stepic.droid.core.presenters.DroppingPresenter
import org.stepic.droid.core.presenters.PersistentCourseListPresenter
import org.stepic.droid.core.presenters.contracts.ContinueCourseView
import org.stepic.droid.core.presenters.contracts.CoursesView
import org.stepic.droid.core.presenters.contracts.DroppingView
import org.stepic.droid.model.Course
import org.stepic.droid.model.Section
import org.stepic.droid.storage.operations.Table
import org.stepic.droid.ui.adapters.CoursesAdapter
import org.stepic.droid.ui.decorators.LeftSpacesDecoration
import org.stepic.droid.ui.decorators.RightMarginForLastItems
import org.stepic.droid.ui.decorators.VerticalSpacesForFirstRowDecoration
import org.stepic.droid.ui.util.StartSnapHelper
import org.stepic.droid.ui.util.initCenteredToolbar
import javax.inject.Inject

class HomeFragment : FragmentBase(), ContinueCourseView, CoursesView, DroppingView {

    companion object {

        fun newInstance(): HomeFragment {
            val args = Bundle()
            val fragment = HomeFragment()
            fragment.arguments = args
            return fragment
        }

        //FIXME: 04.09.17 if adapter.count < ROW_COUNT -> recycler creates extra padding
        private const val ROW_COUNT = 2

    }

    @Inject
    lateinit var courseListPresenter: PersistentCourseListPresenter

    @Inject
    lateinit var droppingPresenter: DroppingPresenter

    @Inject
    lateinit var continueCoursePresenter: ContinueCoursePresenter

    private val courses = ArrayList<Course>()
    private var isScreenCreated: Boolean = false

    override fun injectComponent() {
        App
                .componentManager()
                .courseGeneralComponent()
                .courseListComponentBuilder()
                .build()
                .inject(this)
    }

    override fun onReleaseComponent() {
        App
                .componentManager()
                .releaseCourseGeneralComponent()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View?
            = inflater?.inflate(R.layout.fragment_home, container, false)

    //
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nullifyActivityBackground()
        initCenteredToolbar(R.string.home_title)
        initMyCoursesRecycler()
        continueCoursePresenter.attachView(this)
        courseListPresenter.attachView(this)
        droppingPresenter.attachView(this)

        courseListPresenter.restoreState()
        isScreenCreated = true
    }

    override fun onStart() {
        super.onStart()
        if (isScreenCreated) {
            //reset all data
            isScreenCreated = false
            courses.clear()
            courseListPresenter.refreshData(Table.enrolled, false, false)
        } else {
            //load if not
            courseListPresenter.downloadData(Table.enrolled, false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        continueCoursePresenter.detachView(this)
        courseListPresenter.detachView(this)
        droppingPresenter.detachView(this)
    }

    private fun initMyCoursesRecycler() {
        myCoursesOnHome.layoutManager = GridLayoutManager(context, ROW_COUNT, GridLayoutManager.HORIZONTAL, false)
        myCoursesOnHome.adapter = CoursesAdapter(this, courses, Table.enrolled, continueCoursePresenter, droppingPresenter, false)
        val spacePx = resources.getDimensionPixelSize(R.dimen.course_list_between_items_padding)
        myCoursesOnHome.addItemDecoration(VerticalSpacesForFirstRowDecoration(spacePx, ROW_COUNT))
        myCoursesOnHome.addItemDecoration(LeftSpacesDecoration(spacePx))
        myCoursesOnHome.addItemDecoration(RightMarginForLastItems(resources.getDimensionPixelSize(R.dimen.home_right_recycler_padding_without_extra), ROW_COUNT))
        val snapHelper = StartSnapHelper()
        snapHelper.attachToRecyclerView(myCoursesOnHome)
    }

    override fun onOpenStep(courseId: Long, section: Section, lessonId: Long, unitId: Long, stepPosition: Int) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onAnyProblemWhileContinue(course: Course) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onShowContinueCourseLoadingDialog() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showEmptyCourses() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showConnectionProblem() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showCourses(courses: MutableList<Course>) {
        this.courses.clear()
        this.courses.addAll(courses)
        myCoursesOnHome.adapter.notifyDataSetChanged()
    }

    override fun onUserHasNotPermissionsToDrop() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
