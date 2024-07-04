package com.cafedemetro.pos;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

//1. Annotation to make this a EJB Bean. Choose carefully whether it should be stateless or stateful.
@Stateless
//2. Security annotations, choose carefully what should be filled in.
@RolesAllowed("STAFF")
//3. Transaction type annotation, choose carefully which type of transaction attribute type should be used.
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class OrderMgt /* 4. What should be inherited here? */implements OrderMgtRemote {
    
    //5. Annotation to specify the persistence context name and type.
    @PersistenceContext(unitName = "pos-ejbPU", type=PersistenceContextType.TRANSACTION)
    private EntityManager em;

    @Override
    //6. Do we need to have Security annotations here? If yes, choose carefully what should be filled in.
    //no need
    public String CreateOrder(String branchCode, int numOfSeat) throws Exception {
        String qrCode = "";
        Branches branch = GetBranchByCode(branchCode);

        //Generate QR Code
        qrCode = UUID.randomUUID().toString();

        //Create the Orders object
        Orders newOrder = new Orders();
        newOrder.setBranch(branch);
        newOrder.setCreateDtm(new Date());
        newOrder.setQrCode(qrCode);
        newOrder.setSeats(numOfSeat);
        
        if(GetAvailableSeats(branchCode) >= numOfSeat){
            newOrder.setStatus("A");
        } else {
            newOrder.setStatus("P");
        }

        em.persist(newOrder);

        return qrCode;
    }

    @Override
    //7. Do we need to have Security annotations here? If yes, choose carefully what should be filled in.
    @RolesAllowed("ADMIN")
    public boolean UpdateOrderInDB(String qrCode, String targetStatus) throws Exception {
        Orders existingOrder = GetOrdersByQrCode(qrCode);

        existingOrder.setStatus(targetStatus);
        em.persist(existingOrder);
        
        RecalOrderStatus(qrCode);
        
        return true;
    }

    @Override
    //8. Do we need to have Security annotations here? If yes, choose carefully what should be filled in.
    //no need
    public String PrintOrder(String qrCode) throws Exception {
        Orders orders = GetOrdersByQrCode(qrCode);
        StringBuilder sb = new StringBuilder();
        
        sb.append("Branch Code : ");
        sb.append(orders.getBranch().getBranchCode());
        sb.append("\r\n");
        
        sb.append("QR Code : ");
        sb.append(orders.getQrCode());
        sb.append("\r\n");      
        
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sb.append("Date : ");
        sb.append(fmt.format(orders.getCreateDtm()));
        sb.append("\r\n");
        
        sb.append("Items : ");
        //orders.getDetails().forEach(d -> sb.append(d.getItems().getItemName()).append("\r\n"));
        for(OrderDetails d : orders.getDetails()){
            sb.append(d.getItems().getItemName()).append("\r\n");
        }
        
        return sb.toString();
    }
    
    @Override
    //9. Do we need to have Security annotations here? If yes, choose carefully what should be filled in.
    //no need
    public String CloseOrder(String qrCode) throws Exception {
        Orders orders = GetOrdersByQrCode(qrCode);
        if(orders.getStatus().equals("C")){
            throw new Exception("Orders already completed!");
        }
        
        UpdateOrderInDB(qrCode, "C");
        
        RecalOrderStatus(qrCode);
        
        return PrintOrder(qrCode);
    }
    
    private Orders GetOrdersByQrCode(String qrCode) throws Exception {
        Query qOrders;
        List<Orders> orders = null;
        Orders existingOrder = null;

        qOrders = em.createNamedQuery("Orders.GetOrdersByQrCode");
        qOrders.setParameter("qrCode", qrCode);

        orders = qOrders.getResultList();
        if(orders.isEmpty()){
            throw new Exception("QR Code [" + qrCode + "] is not valid.");
        } else {
            existingOrder = orders.get(0);
        }
        
        return existingOrder;
    }
    
    private Branches GetBranchByCode(String branchCode) throws Exception {
        Query qBranch;
        List<Branches> branches = null;

        qBranch = em.createQuery("select b from Branches b where b.branchCode = :branchCode");
        qBranch.setParameter("branchCode", branchCode);
        branches = qBranch.getResultList();

        if(branches.isEmpty()){
            throw new Exception("Branch Code [" + branchCode + "] is not valid." );
        }
        
        return branches.get(0);
    }
    
    private int GetAvailableSeats(String branchCode) throws Exception {
        Branches branches = GetBranchByCode(branchCode);

        String sql = "SELECT SUM(o.seats) FROM Branches b JOIN b.orders o WHERE b.branchCode = :branchCode AND o.status = 'A'";
        TypedQuery<Long> query = em.createQuery(sql, Long.class);
        query.setParameter("branchCode", branchCode);
        Long sumOfActiveSeats = query.getSingleResult();

        return branches.getTotalSeats() - ((sumOfActiveSeats == null) ? 0 : sumOfActiveSeats.intValue());
    }//end of GetAvailableSeats()
    
    private void RecalOrderStatus(String qrCode) throws Exception {
        Orders orders = GetOrdersByQrCode(qrCode);
        Branches branches = orders.getBranch();
        int availableSeat = GetAvailableSeats(branches.getBranchCode());
        
        String sql = "SELECT o FROM Branches b JOIN b.orders o WHERE b.branchCode = :branchCode AND o.status = 'P' and o.seats <= :seats";
        Query query = em.createQuery(sql);
        query.setParameter("branchCode", branches.getBranchCode());
        query.setParameter("seats", availableSeat);
        List<Orders> allPendingOrders = query.getResultList();
        
        for(Orders o : allPendingOrders){
            o.setStatus("A");
            em.persist(o);
        }//loop all active orders
    }//end of RecalOrderStatus()
}