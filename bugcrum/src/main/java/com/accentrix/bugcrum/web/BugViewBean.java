package com.accentrix.bugcrum.web;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.commons.lang.StringUtils;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.accentrix.bugcrum.domain.bugzilla.Bug;
import com.accentrix.bugcrum.domain.bugzilla.BugStatus;
import com.accentrix.bugcrum.domain.bugzilla.Classification;
import com.accentrix.bugcrum.domain.bugzilla.Component;
import com.accentrix.bugcrum.domain.bugzilla.Product;
import com.accentrix.bugcrum.domain.bugzilla.User;
import com.accentrix.bugcrum.domain.scrum.Sprint;
import com.accentrix.bugcrum.domain.scrum.SprintBug;
import com.accentrix.bugcrum.dto.BugActivityDto;
import com.accentrix.bugcrum.service.BugService;
import com.accentrix.bugcrum.service.BugStatusService;
import com.accentrix.bugcrum.service.ClassificationService;
import com.accentrix.bugcrum.service.ComponentService;
import com.accentrix.bugcrum.service.ProductService;
import com.accentrix.bugcrum.service.SprintBugService;
import com.accentrix.bugcrum.service.SprintService;
import com.accentrix.nttca.dcms.common.cache.ThreadCacheAdministrator;
import com.accentrix.nttca.dcms.common.exception.annotation.WatchException;
import com.accentrix.nttca.dcms.common.exception.handler.DefaultExceptionHandler;
import com.accentrix.nttca.dcms.common.util.CookieUtil;
import com.accentrix.nttca.dcms.common.util.UIUtil;

@ManagedBean
@SessionScoped
@Configurable(preConstruction = true)
@WatchException(update = "msgForm:msgs", exceptionHandler = DefaultExceptionHandler.class)
public class BugViewBean implements Serializable {

    private static final String PRODUCT_ID = "productId";
    private static final String CLASSIFICATION_ID = "classificationId";
    private static final double DEFAULT_EFFORT = 8d;
    private static final long ONE_DATE = 1000L * 60 * 60 * 24;
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(BugViewBean.class);
    private static final SimpleDateFormat SDF = new SimpleDateFormat("MM-dd");

    @Autowired
    private ProductService productService;

    @Autowired
    private ComponentService componentService;

    @Autowired
    private ClassificationService classificationService;

    @Autowired
    private BugStatusService bugStatusService;

    @Autowired
    private SprintService sprintService;

    @Autowired
    private SprintBugService sprintBugService;

    @Autowired
    private BugService bugService;

    private List<BugStatus> bugStatuses;

    private List<Product> products;

    private List<Component> components;

    private List<Classification> classifications;

    private Integer selectProductId;

    private Integer productId;

    private Integer classificationId;

    private List<Integer> selectedComponentIds;

    private String bugId;

    private List<String> selectedBugStatus;

    private String component;

    private Integer sprintId;

    private Product product;

    private LineChartModel chartDataModel;

    private List<Sprint> sprintList;
    private Integer componentId;
    private LazyBugViewDataModel bugsModel;

    private List<Bug> sprintBugList;
    private List<Bug> filteredSprintBugList;
    private Double sprintRemainingTime;
    private Double sprintEstimatedTime;

    private Double totalTime;

    private Integer activeIndex;

    private Sprint sprint;

    private List<User> sprintUsers;
    private Integer userId;

    private String searchDesc;

    private Bug toDeleteBug;

    public BugViewBean() {
        log.info("BugViewBean");
        init();
    }

    // @PostConstruct DO NOT uncomment this line
    public void init() {
        log.info("init");

        classifications = classificationService.findAll();

        String cIdStr = CookieUtil.getCookie(CLASSIFICATION_ID);
        log.info("CLASSIFICATION_ID: {}", cIdStr);
        if (StringUtils.isNotBlank(cIdStr)) {
            classificationId = Integer.valueOf(cIdStr);

            Classification classification = new Classification();
            classification.setId(classificationId);
            products = productService.findAllByClassification(classification);

            String pidStr = CookieUtil.getCookie(PRODUCT_ID);
            log.info("PRODUCT_ID: {}", pidStr);
            if (StringUtils.isNotBlank(pidStr)) {
                productId = Integer.valueOf(pidStr);
            }

        }

        bugStatuses = bugStatusService.findAll();

        bugsModel = new LazyBugViewDataModel(productId, selectedComponentIds, selectedBugStatus);
    }

    private List<Bug> toBugList(List<SprintBug> sbList) {
        log.debug("toBugList, sbList size: {}", sbList.size());
        List<Bug> bugList = new ArrayList<Bug>(sbList.size());
        for (SprintBug sprintBug : sbList) {
            bugList.add(sprintBug.getBug());
        }

        return bugList;
    }

    public void getSpringBugs() {
        log.info("getSpringBugs--------------------------");
        log.info("activeIndex--" + activeIndex);
        // sprintBugList = sprintBugService.findBySprintId(sprintId);
        // UIUtil.update("viewSprintform:sprintTabView:sprintBugList");
    }

    public List<Sprint> findAllSprints() {
        return sprintService.findAll();
    }

    public void saveSprint() {
        log.info("saveSprint");

        if (productId != null) {
            log.info("productId: {}", productId);
            Product pro = productService.findOne(productId);
            sprint.setProduct(pro);
        }

        if (UIUtil.validate(sprint)) {

            sprintService.save(sprint);

            refreshSprintLayout(false);

            UIUtil.hide("createSprintDlgWidget");
        }

        ThreadCacheAdministrator.getInstance().destroyCurrentThreadCache();
    }

    public void onSprintChange() {

        sprint = sprintService.findOne(sprintId);

        sprintBugList = toBugList(sprintBugService.findBySprintId(sprintId));

        sprintRemainingTime = sumRemainingTime(sprintBugList);
        sprintEstimatedTime = sumEstimatedTime(sprintBugList);

        sprintUsers = new ArrayList<User>();
        Map<Integer, User> filterMap = new HashMap<Integer, User>();
        for (Bug bug : sprintBugList) {
            User user = bug.getAssignedTo();
            if (filterMap.get(user.getId()) == null) {
                filterMap.put(user.getId(), user);
                sprintUsers.add(user);
            }
        }

        chartDataModel = null;

        UIUtil.execute("PF('sprintBugDataTable').clearFilters()");
    }

    public void viewBurnDownChart() {
        log.debug("view burn down chart function");
        log.debug("view sprint id is {}", sprintId);
        log.debug("view user id is {}", userId);
        createChartDateModel(sprintId, userId);
    }

    public void onClassificationChange() {
        log.info("classificationId: {}", classificationId);
        if (classificationId != null) {
            CookieUtil.addCookie(CLASSIFICATION_ID, "" + classificationId);

            Classification classification = new Classification();
            classification.setId(classificationId);
            products = productService.findAllByClassification(classification);

            for (Product p : products) {
                log.info("p: {}", p.getName());
            }

        } else {
            products = Collections.emptyList();
        }

    }

    public void onProductChange() {
        log.info("onProductChange");
        log.info("productId: {}", productId);

        if (productId != null) {
            CookieUtil.addCookie(PRODUCT_ID, "" + productId);

            Product pro = productService.findOne(productId);
            components = componentService.findAllByProduct(pro);
            log.info("components size: {}", components.size());

            selectedComponentIds = null;
        } else {
            log.info("productId is null");
            components = Collections.emptyList();
        }

        refreshSprintLayout(true);

        bugsModel = new LazyBugViewDataModel(productId, selectedComponentIds, selectedBugStatus);
    }

    public void refreshSprintLayout(boolean onProductChange) {
        log.info("refreshSprintLayout");
        log.info("productId: {}", productId);

        if (productId != null) {

            sprintList = sprintService.findByProductId(productId);
            if (!sprintList.isEmpty() && onProductChange) {
                sprintId = sprintList.get(0).getId();
                onSprintChange();
            } else {
                sprintBugList = Collections.emptyList();
            }

        } else {
            log.info("productId is null");
            sprintList = Collections.emptyList();
            sprintBugList = Collections.emptyList();
        }
    }

    public void search() {

        log.info("productId: {}", productId);
        // log.info("selectedComponentIds: {}", selectedComponentIds);
        log.info("selectedBugStatus: {}", selectedBugStatus);
        log.info("bugId: {}", bugId);

        if (productId != null && productId == 0) {
            productId = null;
        }

        if (selectedComponentIds == null) {
            log.info("selectedComponentIds is null");
        } else {
            for (Object cid : selectedComponentIds) {
                log.info("class: {}, v: {}", cid.getClass(), cid);
            }
        }

        totalTime = bugService.sumEstimatedTime(productId, selectedComponentIds, selectedBugStatus,
                searchDesc);

        bugsModel.setProductId(productId);
        bugsModel.setSelectedComponentIds(selectedComponentIds);
        bugsModel.setSelectedBugStatus(selectedBugStatus);
        bugsModel.setSearchDesc(searchDesc);

        log.info("search done");
    }

    public void addToSprint(Bug bug) {
        log.info("addToSprint, sprintId: {}, bugId: {}", sprintId, bug.getId());
        SprintBug sb = new SprintBug();
        sb.setBug(bug);
        sb.setSprint(sprintService.findOne(sprintId));

        sprintBugService.save(sb);

        if (sprintBugList != null) {
            sprintBugList.add(bug);
        } else {
            sprintBugList = Arrays.asList(bug);
        }

        sprintRemainingTime = sumRemainingTime(sprintBugList);
        sprintEstimatedTime = sumEstimatedTime(sprintBugList);

        UIUtil.execute("PF('sprintBugDataTable').clearFilters()");
    }

    public void removeBugFromSprint() {
        if (toDeleteBug == null) {
            log.warn("toDeleteBug is null");
            return;
        }

        log.info("delOfSprint1, sprintId1: {}, bugId: {}", sprintId, toDeleteBug.getId());
        SprintBug sb = sprintBugService.findBySprintIdAndBugId(sprintId, toDeleteBug.getId());
        sprintBugService.delete(sb);
        if (sprintBugList.size() != 0) {
            sprintBugList.remove(toDeleteBug);
        }

        sprintRemainingTime = sumRemainingTime(sprintBugList);
        sprintEstimatedTime = sumEstimatedTime(sprintBugList);

        UIUtil.execute("PF('sprintBugDataTable').clearFilters()");
    }

    public boolean isDisabled(Bug bug) {
        SprintBug sb = sprintBugService.findBySprintIdAndBugId(sprintId, bug.getId());
        if (sb != null) {
            return true;
        }
        return false;
    }

    public void initCreateSprint(String action) {
        log.info("initCreateSprint, action:{}", action);

        UIUtil.reset("createSprintform:createSprintPanel");

        if ("create".equals(action)) {
            sprint = new Sprint();
        } else {
            sprint = sprintService.findOne(sprintId);
        }
    }

    public void deleteSprint() {
        sprintService.delete(sprintId);
        refreshSprintLayout(false);
    }

    // ---------------------- Burn Down Chart Report---------------------------
    private static final String BURN_DOWN_CHART_Y_LABEL = "Total Man Hours";
    private static final String BURN_DOWN_CHART_X_LABEL = "Dates";
    private static final String BURN_DOWN_CHART_TITLE_SUFFIX = " Sprint Burn Down Chart";

    private void createChartDateModel(Integer sprintId, Integer userId) {
        chartDataModel = new LineChartModel();
        if (sprintId == null) {
            log.info("sprintId is null");
            return;
        }

        List<SprintBug> sprintBugs = null;
        if (userId != null && userId > 0) {
            sprintBugs = sprintBugService.findBy(sprint.getId(), userId);
        } else {
            // sprintBugs = sprint.getSprintBugs();
            log.debug("findBySprintId start");
            sprintBugs = sprintBugService.findBySprintId(sprintId);
            log.debug("findBySprintId done");
        }

        List<Bug> bugs = toBugList(sprintBugs);

        chartDataModel.addSeries(calculateIdeal(bugs));
        chartDataModel.addSeries(calculateReality(bugs));

        chartDataModel.setTitle(sprint.getName() + BURN_DOWN_CHART_TITLE_SUFFIX);
        chartDataModel.getAxis(AxisType.Y).setLabel(BURN_DOWN_CHART_Y_LABEL);

        chartDataModel.setLegendPosition("e");
        chartDataModel.setShowPointLabels(true);
        chartDataModel.getAxes().put(AxisType.X, new CategoryAxis(BURN_DOWN_CHART_X_LABEL));
    }

    private ChartSeries calculateIdeal(List<Bug> bugs) {
        log.debug("calculateTarget, bugs size: {}", bugs.size());
        ChartSeries series = new ChartSeries();
        series.setLabel("Ideal");
        if (bugs == null || bugs.isEmpty()) {
            log.info("sprintBugs isEmpty");
            return series;
        }

        double totalManHours = getSprintHours(sprint.getStartDate(), bugs);

        // Double totalManHours = sumRemainingTime(bugs, sprint.getStartDate());

        int days = this.calculateDiscrepantDays(sprint);
        log.info("days: {}", days);

        // calculate working days
        Calendar calendar = getPreDateCalendar(sprint.getStartDate());
        int totalOffDays = 0;
        for (int i = 0; i <= days; i++) {

            // ignore pre day
            if (i > 0) {
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                if (dayOfWeek == Calendar.SUNDAY || dayOfWeek == Calendar.SATURDAY) {
                    totalOffDays++;
                }
            }

            calendar.add(Calendar.DATE, 1);
        }

        int workDays = days - totalOffDays;
        log.info("workDays: {}", workDays);
        double perDay = totalManHours / (double) workDays;
        log.info("perDay: {}", perDay);

        calendar = getPreDateCalendar(sprint.getStartDate());
        for (int i = 0; i <= days; i++) {
            if (i == 0) {
                series.set(SDF.format(calendar.getTime()), totalManHours);
            } else {
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                if (dayOfWeek != Calendar.SUNDAY && dayOfWeek != Calendar.SATURDAY) {
                    totalManHours -= perDay;
                }

                series.set(SDF.format(calendar.getTime()), totalManHours);
            }

            calendar.add(Calendar.DATE, 1);
        }

        return series;
    }

    private double getSprintHours(Date time, List<Bug> bugs) {
        double remainingHours = 0d;
        for (Bug bug : bugs) {

            if (bugService.isClosedBefor(bug, time)) {
                log.debug("bug {} is CLOSED", bug.getId());
                continue;
            }

            BugActivityDto dto = bugService.getLastRemainingTime(bug, time);
            if (dto == null) {
                dto = bugService.getFirstRemainingTime(bug, time);
                if (dto == null) {
                    remainingHours += getRemainingTime(bug);
                } else {
                    remainingHours += Double.valueOf(dto.getAddedValue());
                }
            } else {
                remainingHours += Double.valueOf(dto.getAddedValue());
            }

        }

        return remainingHours;
    }

    private ChartSeries calculateReality(List<Bug> bugs) {
        ChartSeries series = new ChartSeries();
        series.setLabel("Remaining");
        if (bugs == null || bugs.isEmpty()) {
            return series;
        }

        int days = this.calculateDiscrepantDays(sprint);
        Calendar calendar = getPreDateCalendar(sprint.getStartDate());
        Date now = new Date(System.currentTimeMillis() + ONE_DATE);
        for (int i = 0; i <= days; i++) {

            if (!calendar.getTime().before(now)) {
                break;
            }

            double remainingHours = 0d;

            if (i == 0) {
                remainingHours = getSprintHours(sprint.getStartDate(), bugs);
            } else {
                remainingHours = getRemainingHours(calendar.getTime(), bugs);
            }

            String key = SDF.format(calendar.getTime());
            series.set(key, remainingHours);
            log.info("time:{}, remainingHours:{}", key, remainingHours);

            calendar.add(Calendar.DATE, 1);
        }
        return series;
    }

    private double getRemainingHours(Date time, List<Bug> bugs) {
        double remainingHours = 0d;
        for (Bug bug : bugs) {

            if (bugService.isClosedBefor(bug, time)) {
                log.debug("bug {} is CLOSED", bug.getId());
                continue;
            }

            BugActivityDto dto = bugService.getLastRemainingTime(bug, time);
            if (dto == null) {
                remainingHours += getRemainingTime(bug);
            } else {
                remainingHours += Double.valueOf(dto.getAddedValue());
            }

        }

        return remainingHours;
    }

    private double sumRemainingTime(List<Bug> bugs) {
        log.debug("calculateTotalManHours, bugs size: {}", bugs.size());
        // calculate total man hours of task
        double totalManHours = 0D;
        for (Bug bug : bugs) {
            Double estimatedTime = getRemainingTime(bug);
            totalManHours += estimatedTime;
        }
        log.debug("total man hours is {}", totalManHours);
        return totalManHours;
    }

    private double sumEstimatedTime(List<Bug> bugs) {
        log.debug("sumEstimatedTime, bugs size: {}", bugs.size());
        // calculate total man hours of task
        double totalManHours = 0D;
        for (Bug bug : bugs) {
            Double estimatedTime = getEstimatedTime(bug);
            totalManHours += estimatedTime;
        }
        log.debug("totalManHours {}", totalManHours);
        return totalManHours;
    }

    private Double getRemainingTime(Bug bug) {

        Double remainingTime = bug.getRemainingTime();
        if (isZero(remainingTime)) {
            remainingTime = 0d;
        }
        return remainingTime;
    }

    private Double getEstimatedTime(Bug bug) {

        Double estimatedTime = bug.getEstimatedTime();
        if (isZero(estimatedTime)) {
            log.info("bug {} not set estimatedTime, set default to 8", bug.getId());
            estimatedTime = DEFAULT_EFFORT;
        }

        return estimatedTime;
    }

    private boolean isZero(Double estimatedTime) {
        return estimatedTime == null || estimatedTime < 0.0000005;
    }

    private int calculateDiscrepantDays(Sprint sprint) {
        // calculate start date - end date
        int days = (int) ((sprint.getEndDate().getTime() - sprint.getStartDate().getTime()) / ONE_DATE);
        log.debug("start date - end date , days is {}", days);
        return days + 1;
    }

    private Calendar getPreDateCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(date.getTime() - 1000L));
        return calendar;
    }

    public List<Component> getComponents() {
        return components;
    }

    public Integer getSelectProductId() {
        return selectProductId;
    }

    public void setSelectProductId(Integer selectProductId) {
        this.selectProductId = selectProductId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getComponentId() {
        return componentId;
    }

    public void setComponentId(Integer componentId) {
        this.componentId = componentId;
    }

    public void setBugStatuses(List<BugStatus> bugStatuses) {
        this.bugStatuses = bugStatuses;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public void setComponents(List<Component> components) {
        this.components = components;
    }

    public void setClassifications(List<Classification> classifications) {
        this.classifications = classifications;
    }

    public List<Integer> getSelectedComponentIds() {
        return selectedComponentIds;
    }

    public void setSelectedComponentIds(List<Integer> selectedComponentIds) {
        this.selectedComponentIds = selectedComponentIds;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<BugStatus> getBugStatuses() {
        return bugStatuses;
    }

    public List<Classification> getClassifications() {
        return classifications;
    }

    public List<Product> getProducts() {
        return products;
    }

    public Integer getClassificationId() {
        return classificationId;
    }

    public void setClassificationId(Integer classificationId) {
        this.classificationId = classificationId;
    }

    public List<String> getSelectedBugStatus() {
        return selectedBugStatus;
    }

    public void setSelectedBugStatus(List<String> selectedBugStatus) {
        this.selectedBugStatus = selectedBugStatus;
    }

    public String getBugId() {
        return bugId;
    }

    public void setBugId(String bugId) {
        this.bugId = bugId;
    }

    public Sprint getSprint() {
        return sprint;
    }

    public LineChartModel getChartDataModel() {
        return chartDataModel;
    }

    public void setChartDataModel(LineChartModel chartDataModel) {
        this.chartDataModel = chartDataModel;
    }

    public LazyDataModel<Bug> getBugsModel() {
        return bugsModel;
    }

    public List<Sprint> getSprintList() {
        return sprintList;
    }

    public void setSprintList(List<Sprint> sprintList) {
        this.sprintList = sprintList;
    }

    public List<Bug> getSprintBugList() {
        return sprintBugList;
    }

    public void setSprintBugList(List<Bug> sprintBugList) {
        this.sprintBugList = sprintBugList;
    }

    public Integer getActiveIndex() {
        return activeIndex;
    }

    public void setActiveIndex(Integer activeIndex) {
        this.activeIndex = activeIndex;
    }

    public Integer getSprintId() {
        return sprintId;
    }

    public void setSprintId(Integer sprintId) {
        this.sprintId = sprintId;
    }

    public List<User> getSprintUsers() {
        return sprintUsers;
    }

    public void setSprintUsers(List<User> sprintUsers) {
        this.sprintUsers = sprintUsers;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Double getTotalTime() {
        return totalTime;
    }

    public Double getSprintRemainingTime() {
        return sprintRemainingTime;
    }

    public Double getSprintEstimatedTime() {
        return sprintEstimatedTime;
    }

    public List<Bug> getFilteredSprintBugList() {
        return filteredSprintBugList;
    }

    public void setFilteredSprintBugList(List<Bug> filteredSprintBugList) {
        this.filteredSprintBugList = filteredSprintBugList;
    }

    public String getSearchDesc() {
        return searchDesc;
    }

    public void setSearchDesc(String searchDesc) {
        this.searchDesc = searchDesc;
    }

    public void setToDeleteBug(Bug toDeleteBug) {
        this.toDeleteBug = toDeleteBug;
    }

}
