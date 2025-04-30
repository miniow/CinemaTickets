/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package michal.com.domain.DomainExceptions;

/**
 *
 * @author zymci
 */
public abstract class DomainException extends RuntimeException {
    public DomainException(String message) {
        super(message);
    }
}