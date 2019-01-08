<%=packageName ? "package ${packageName}" : ''%>

import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.NO_CONTENT
import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY

class ${className}Controller {

    ${className}Service ${propertyName}Service

    static responseFormats = ['json', 'xml']
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond ${propertyName}Service.list(params), model:[${propertyName}Count: ${propertyName}Service.count()]
    }

    def show(Long id) {
        respond ${propertyName}Service.get(id)
    }

    def save(${className} ${propertyName}) {
        if (${propertyName} == null) {
            render status: NOT_FOUND
            return
        }

        try {
            ${propertyName}Service.save(${propertyName})
        } catch (ValidationException e) {
            respond ${propertyName}.errors, view:'create'
            return
        }

        respond ${propertyName}, [status: CREATED, view:"show"]
    }

    def update(${className} ${propertyName}) {
        if (${propertyName} == null) {
            render status: NOT_FOUND
            return
        }

        try {
            ${propertyName}Service.save(${propertyName})
        } catch (ValidationException e) {
            respond ${propertyName}.errors, view:'edit'
            return
        }

        respond ${propertyName}, [status: OK, view:"show"]
    }

    def delete(Long id) {
        if (id == null) {
            render status: NOT_FOUND
            return
        }

        ${propertyName}Service.delete(id)

        render status: NO_CONTENT
    }
}
